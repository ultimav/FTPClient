package ftp;

/**
 * Represent server files.
 *
 * @author Vojko Vladimir
 */
public class RemoteFile {

    private static final int PERMISSIONS_ID = 0;
    private static final int TYPE_ID = 1;
    private static final int OWNER_ID = 2;
    private static final int GROUP_ID = 3;
    private static final int SIZE_ID = 4;
    private static final int MONTH_ID = 5;
    private static final int DAY_ID = 6;
    private static final int TIME_ID = 7;
    private static final int NAME_ID = 8;

    private String permissions;
    private int type;
    private String owner;
    private String group;
    private int size;
    private String month;
    private int day;
    private String time;
    private String name;
    
    public RemoteFile(String fileProperties) {
        String[] properties = fileProperties.split("\\s+");

        permissions = properties[PERMISSIONS_ID];
        type = Integer.parseInt(properties[TYPE_ID]);
        owner = properties[OWNER_ID];
        group = properties[GROUP_ID];
        size = Integer.parseInt(properties[SIZE_ID]);
        month = properties[MONTH_ID];
        day = Integer.parseInt(properties[DAY_ID]);
        time = properties[TIME_ID];
        name = properties[NAME_ID];
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getGroup() {
        return group;
    }

    public int getSize() {
        return size;
    }

    public String getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getPermissions() {
        return permissions;
    }

    public boolean isFolder() {
        return type != 1;
    }

}
