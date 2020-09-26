package sky.starry.community.enums;

public enum NotificationStatusEnum {
    READ(1),
    UNREAD(0);

    private int Status;

    public int getStatus() {
        return Status;
    }

    NotificationStatusEnum(int status) {
        Status = status;
    }
}
