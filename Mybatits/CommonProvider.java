

import org.apache.ibatis.mapping.MappedStatement;
import org.joda.time.DateTime;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.annotation.Version;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.*;
import tk.mybatis.mapper.version.VersionException;

import java.util.Date;
import java.util.Set;


public class CommonProvider extends MapperTemplate {

    private final String DEFAULT_FIELD_CREATOR = "creator";
    private final String DEFAULT_FIELD_MODIFIER = "modifier";
    private final String DEFAULT_FIELD_GMT_CREATE = "gmt_create";
    private final String DEFAULT_FIELD_GMT_MODIFIED = "gmt_modified";

    public CommonProviCommonProviderder(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String insert(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        processKey(sql, entityClass, ms, columnList);
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
        boolean standardDomainFlag = DomainObject.class.isAssignableFrom(entityClass);
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            //处理标准字段的默认值
            if (standardDomainFlag) {
                String columnName = column.getColumn();
                if (DEFAULT_FIELD_CREATOR.equalsIgnoreCase(columnName) || DEFAULT_FIELD_MODIFIER.equalsIgnoreCase(columnName)) {
                    sql.append(SqlHelper.getIfIsNull(column, "'SYS',", true));
                    sql.append(SqlHelper.getIfNotNull(column, "#{" + column.getProperty() + "},", true));
                    continue;
                } else if (DEFAULT_FIELD_GMT_CREATE.equalsIgnoreCase(columnName) || DEFAULT_FIELD_GMT_MODIFIED.equalsIgnoreCase(columnName)) {
                    sql.append(SqlHelper.getIfIsNull(column, "NOW(),", false));
                    sql.append(SqlHelper.getIfNotNull(column, "#{" + column.getProperty() + "},", false));
                    continue;
                }
            }
            //优先使用传入的属性值,当原属性property!=null时，用原属性
            //自增的情况下,如果默认有值,就会备份到property_cache中,所以这里需要先判断备份的值是否存在
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder(null, "_cache", ",")));
            } else {
                //其他情况值仍然存在原property中
                sql.append(SqlHelper.getIfNotNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
            }
            //当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder() + ","));
            } else {
                //当null的时候，如果不指定jdbcType，oracle可能会报异常，指定VARCHAR不影响其他
                sql.append(SqlHelper.getIfIsNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
            }
        }
        sql.append("</trim>");
        return sql.toString();
    }

    public String insertList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        boolean standardDomainFlag = DomainObject.class.isAssignableFrom(entityClass);
        for (EntityColumn column : columnList) {
            column.setProperty("record." + column.getProperty());
            //处理标准字段的默认值
            if (standardDomainFlag) {
                String columnName = column.getColumn();
                if (DEFAULT_FIELD_CREATOR.equalsIgnoreCase(columnName) || DEFAULT_FIELD_MODIFIER.equalsIgnoreCase(columnName)) {
                    sql.append(SqlHelper.getIfIsNull(column, "'SYS',", true));
                    sql.append(SqlHelper.getIfNotNull(column, "#{" + column.getProperty() + "},", true));
                    continue;
                } else if (DEFAULT_FIELD_GMT_CREATE.equalsIgnoreCase(columnName) || DEFAULT_FIELD_GMT_MODIFIED.equalsIgnoreCase(columnName)) {
                    sql.append(SqlHelper.getIfIsNull(column, "NOW(),", false));
                    sql.append(SqlHelper.getIfNotNull(column, "#{" + column.getProperty() + "},", false));
                    continue;
                }
            }
            sql.append("#{").append(column.getProperty()).append("},");
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
    }

    /**
     * 通过主键更新
     */
    public String updateById(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append(updateSetColumns(entityClass, null, false, false));
        sql.append(SqlHelper.wherePKColumns(entityClass, true));
        return sql.toString();
    }

    private void processKey(StringBuilder sql, Class<?> entityClass, MappedStatement ms, Set<EntityColumn> columnList) {
        //Identity列只能有一个
        Boolean hasIdentityKey = false;
        //先处理cache或bind节点
        for (EntityColumn column : columnList) {
            if (column.isIdentity()) {
                //这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长
                //这是一个bind节点
                sql.append(SqlHelper.getBindCache(column));
                //如果是Identity列，就需要插入selectKey
                //如果已经存在Identity列，抛出异常
                if (hasIdentityKey) {
                    //jdbc类型只需要添加一次
                    if (column.getGenerator() != null && column.getGenerator().equals("JDBC")) {
                        continue;
                    }
                    throw new MapperException(
                        ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                //插入selectKey
                SelectKeyHelper.newSelectKeyMappedStatement(ms, column, entityClass, isBEFORE(), getIDENTITY(column));
                hasIdentityKey = true;
            } else if (column.getGenIdClass() != null) {
                sql.append("<bind name=\"").append(column.getColumn()).append(
                    "GenIdBind\" value=\"@tk.mybatis.mapper.genid.GenIdUtil@genId(");
                sql.append("_parameter").append(", '").append(column.getProperty()).append("'");
                sql.append(", @").append(column.getGenIdClass().getCanonicalName()).append("@class");
                sql.append(", '").append(tableName(entityClass)).append("'");
                sql.append(", '").append(column.getColumn()).append("')");
                sql.append("\"/>");
            }

        }
    }

    /**
     * update set列
     *
     * @param entityClass
     * @param entityName  实体映射名
     * @param notNull     是否判断!=null
     * @param notEmpty    是否判断String类型!=''
     * @return
     */
    private String updateSetColumns(Class<?> entityClass, String entityName, boolean notNull, boolean notEmpty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<set>");
        //获取全部列
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        //对乐观锁的支持
        EntityColumn versionColumn = null;
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnSet) {
            boolean standardDomainFlag = DomainObject.class.isAssignableFrom(entityClass);
            if (standardDomainFlag) {
                String columnName = column.getColumn();
                if (DEFAULT_FIELD_MODIFIER.equalsIgnoreCase(columnName)) {
                    sql.append(SqlHelper.getIfIsNull(column, "modifier = 'SYS',", true));
                    sql.append(SqlHelper.getIfNotNull(column, "modifier = #{modifier},", true));
                    continue;
                } else if (DEFAULT_FIELD_GMT_MODIFIED.equalsIgnoreCase(columnName)) {
                    sql.append(columnName).append(" = ").append("NOW()").append(",");
                    continue;
                }
            }
            if (column.getEntityField().isAnnotationPresent(Version.class)) {
                if (versionColumn != null) {
                    throw new VersionException(
                        entityClass.getCanonicalName() + " 中包含多个带有 @Version 注解的字段，一个类中只能存在一个带有 @Version 注解的字段!");
                }
                versionColumn = column;
            }
            if (!column.isId() && column.isUpdatable()) {
                if (column == versionColumn) {
                    Version version = versionColumn.getEntityField().getAnnotation(Version.class);
                    String versionClass = version.nextVersion().getCanonicalName();
                    //version = ${@tk.mybatis.mapper.version@nextVersionClass("versionClass", version)}
                    sql.append(column.getColumn())
                        .append(" = ${@tk.mybatis.mapper.version.VersionUtil@nextVersion(")
                        .append("@").append(versionClass).append("@class, ")
                        .append(column.getProperty()).append(")},");
                } else if (notNull) {
                    sql.append(SqlHelper
                        .getIfNotNull(entityName, column, column.getColumnEqualsHolder(entityName) + ",", notEmpty));
                } else {
                    sql.append(column.getColumnEqualsHolder(entityName) + ",");
                }
            }
        }
        sql.append("</set>");
        return sql.toString();
    }
}
