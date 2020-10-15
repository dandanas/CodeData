
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 领域模型基类
 *
 * @author suchao
 * @since 2019-08-30 15:19
 */
public class DomainObject implements Serializable {

    private static final long serialVersionUID = -9063487387470198024L;

    /**
     * id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 修改者
     */
    private String modifier;

    /**
     * 设置insert时的基础信息
     */
    public void setDefaultValue() {
        Date now = new Date();
        if (this.gmtCreate == null) {
            this.setGmtCreate(now);
        }

        if (this.gmtModified == null) {
            this.setGmtModified(now);
        }

        if (StringUtils.isBlank(this.creator)) {
            this.setCreator("SYS");
        }

        if (StringUtils.isBlank(this.modifier)) {
            this.setModifier("SYS");
        }
    }

    /**
     * 设置update时的基础信息
     */
    public void setDefaultValueForUpdate() {
        if (StringUtils.isBlank(this.modifier)) {
            this.setModifier("SYS");
        }
        Date now = new Date();
        this.setGmtModified(now);
    }

    /**
     * Getter method for property id.
     *
     * @return property value of id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter method for property id.
     *
     * @param id value to be assigned to property id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter method for property gmtCreate.
     *
     * @return property value of gmtCreate
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * Setter method for property gmtCreate.
     *
     * @param gmtCreate value to be assigned to property gmtCreate
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * Getter method for property gmtModified.
     *
     * @return property value of gmtModified
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * Setter method for property gmtModified.
     *
     * @param gmtModified value to be assigned to property gmtModified
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * Getter method for property creator.
     *
     * @return property value of creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Setter method for property creator.
     *
     * @param creator value to be assigned to property creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * Getter method for property modifier.
     *
     * @return property value of modifier
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * Setter method for property modifier.
     *
     * @param modifier value to be assigned to property modifier
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * 重写ToString
     *
     * @return String
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}