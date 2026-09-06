package com.sellgirl.castScreen.android.permission;

public class PermissionRequest {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_OVERLAY = 1;
    public static final int TYPE_ACCESSIBILITY = 2;
    public static final int TYPE_MEDIA = 3;
    private String permissionName;
    private String permissionDesc;
    private int requestCode;
    private Runnable onGrantedAction;
    private Runnable onDeniedAction;
    private boolean isRequired; // 是否必需权限
    private int permissionType; // 新增：权限类型

    // 构造函数、getter、setter
    public PermissionRequest(String name, String desc, int code,
                             Runnable onGranted, Runnable onDenied, boolean required) {
        this.permissionName = name;
        this.permissionDesc = desc;
        this.requestCode = code;
        this.onGrantedAction = onGranted;
        this.onDeniedAction = onDenied;
        this.isRequired = required;
    }

    // Builder模式创建
    public static class Builder {
        private String name;
        private String desc;
        private int code;
        private Runnable onGranted;
        private Runnable onDenied;
        private boolean required = true;
        private int type = TYPE_NORMAL;

        public Builder setName(String name) { this.name = name; return this; }
        public Builder setDesc(String desc) { this.desc = desc; return this; }
        public Builder setCode(int code) { this.code = code; return this; }
        public Builder setOnGranted(Runnable action) { this.onGranted = action; return this; }
        public Builder setOnDenied(Runnable action) { this.onDenied = action; return this; }
        public Builder setRequired(boolean required) { this.required = required; return this; }
        public Builder setType(int type) { this.type = type; return this; }

        public PermissionRequest build() {
            PermissionRequest request = new PermissionRequest(name, desc, code, onGranted, onDenied, required);
            request.setPermissionType(type);
            return request;
        }
    }

    //--------------------------------other------------------------

    public String getPermissionName() {
        return permissionName;
    }

//    public void setPermissionName(String permissionName) {
//        this.permissionName = permissionName;
//    }

    public String getPermissionDesc() {
        return permissionDesc;
    }

//    public void setPermissionDesc(String permissionDesc) {
//        this.permissionDesc = permissionDesc;
//    }

    public int getRequestCode() {
        return requestCode;
    }

//    public void setRequestCode(int requestCode) {
//        this.requestCode = requestCode;
//    }

    public Runnable getOnGrantedAction() {
        return onGrantedAction;
    }

//    public void setOnGrantedAction(Runnable onGrantedAction) {
//        this.onGrantedAction = onGrantedAction;
//    }

    public Runnable getOnDeniedAction() {
        return onDeniedAction;
    }

    //    public void setOnDeniedAction(Runnable onDeniedAction) {
//        this.onDeniedAction = onDeniedAction;
//    }
    // getter 和 setter
    public int getPermissionType() { return permissionType; }
    public void setPermissionType(int type) { this.permissionType = type; }
}
