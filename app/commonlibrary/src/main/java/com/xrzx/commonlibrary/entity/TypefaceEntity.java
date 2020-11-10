package com.xrzx.commonlibrary.entity;

/**
 * @Description
 * @Author ks
 * @Date 2020/11/10 20:08
 */
public class TypefaceEntity {

    /**
     * 编号
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 资源编号
     */
    private Integer resourceNumber;

    public TypefaceEntity(Integer id, String name, Integer resourceNumber) {
        this.id = id;
        this.name = name;
        this.resourceNumber = resourceNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getResourceNumber() {
        return resourceNumber;
    }

    public void setResourceNumber(Integer resourceNumber) {
        this.resourceNumber = resourceNumber;
    }

    @Override
    public String toString() {
        return "TypefaceEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", resourceNumber=" + resourceNumber +
                '}';
    }
}
