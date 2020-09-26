package sky.starry.community.enums;

public enum NotificationEnum {
    REPLAR_QUESTION(1,"回复了问题"),
    REPLAR_COMMENT(2,"回复了评论")
    ;
    private int type;
    private String name;

    public static String nameOfValue(Integer type) {
        for (NotificationEnum notificationEnum : NotificationEnum.values()) {
            if(notificationEnum.getType() == type){
                return notificationEnum.getName();
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }
}
