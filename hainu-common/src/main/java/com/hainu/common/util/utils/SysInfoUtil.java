package com.hainu.common.util.utils;

import oshi.SystemInfo;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.util.FormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project：pipe-gallery
 * @Package：com.hainu.utils
 * @Date：2021/8/26 17:47
 * @Author：yy188
 * @Address： HaiKou·China
 * @Description:
 * @Modified By: yy188
 */
public class SysInfoUtil {


    private static void printFileSystem(FileSystem fileSystem) {

        //
        // System.out.format(" File Descriptors: %d/%d%n", fileSystem.getOpenFileDescriptors(),
        //         fileSystem.getMaxFileDescriptors());

        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long usable = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            System.out.format(
                    " %s (%s) [%s] %s of %s free (%.1f%%) is %s "
                            + (fs.getLogicalVolume() != null && fs.getLogicalVolume().length() > 0 ? "[%s]" : "%s")
                            + " and is mounted at %s%n",
                    fs.getName(), fs.getDescription().isEmpty() ? "file system" : fs.getDescription(), fs.getType(),
                    FormatUtil.formatBytes(usable), FormatUtil.formatBytes(fs.getTotalSpace()), 100d * usable / total,
                    fs.getVolume(), fs.getLogicalVolume(), fs.getMount());
        }
    }

    public static List<OSFileStore> getFileSystemInfo() {

        List<OSFileStore> fileInfo = new ArrayList<>();
        List<OSFileStore> fsArray = new SystemInfo()
                .getOperatingSystem()
                .getFileSystem()
                .getFileStores();


        for (OSFileStore fs : fsArray) {
            if (!fs.getDescription().equals("Mount Point")) {
                break;
            }
            fileInfo.add(fs);

        }
        return fileInfo;

    }


}
