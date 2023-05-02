package lahmmp.budget.userManagement.administrator;

public class AdminErr {

    private Boolean employeenumberErr;
    private Boolean roleErr;
    private Boolean employeenameErr;

    public Boolean isEmployeenumberErr() {
        return this.employeenumberErr;
    }

    public void setEmployeenumberErr(final Boolean employeenumberErr) {
        this.employeenumberErr = employeenumberErr;
    }

    public Boolean isRoleErr() {
        return this.roleErr;
    }

    public void setRoleErr(final Boolean roleErr) {
        this.roleErr = roleErr;
    }

    public Boolean isEmployeenameErr() {return employeenameErr; }

    public void setEmployeenameErr(Boolean employeenameErr) {
        this.employeenameErr = employeenameErr;
    }

    public void resetErr() {
        employeenumberErr = false;
        roleErr = false;
        employeenameErr = false;
    }

}

