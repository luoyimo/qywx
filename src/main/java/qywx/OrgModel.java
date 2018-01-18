package qywx;

/**
 * @author huqi
 * @create 2018-01-17 17:40
 **/
public class OrgModel {

    private boolean createDeptGroup;
    private String name;
    private Integer id;
    private boolean autoAddUser;
    private Integer parentid;

    @Override
    public String toString() {
        return "OrgModel{" +
                "createDeptGroup=" + createDeptGroup +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", autoAddUser=" + autoAddUser +
                ", parentid=" + parentid +
                '}';
    }

    public boolean isCreateDeptGroup() {
        return createDeptGroup;
    }

    public void setCreateDeptGroup(boolean createDeptGroup) {
        this.createDeptGroup = createDeptGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isAutoAddUser() {
        return autoAddUser;
    }

    public void setAutoAddUser(boolean autoAddUser) {
        this.autoAddUser = autoAddUser;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }
}
