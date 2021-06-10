package cn.wwl.textPatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class TextPatchTools {

    private static Map<String,Object> config;
    private static Map<String,String> patchMap;
    private static List<String> patchedList;

    private static File langFile;
    private static File patchFile;
    private static boolean patchDefault;

    private static final AtomicInteger count = new AtomicInteger(1);
    public static void preInit() {
        FileUtils.checkAndInitConfig();
        config = FileUtils.getConfig();
        patchMap = FileUtils.getPatchMap();
        patchedList = new ArrayList<>();
        patchDefault = (Boolean) config.get(FileUtils.PATCH_DEFAULT_LANGUAGE_FILE);
    }

    public static void start() {
        checkFileExists();
        BufferedReader fileReader = FileUtils.createFileReader(langFile);
        StringBuffer buffer = new StringBuffer();
        fileReader.lines().forEach(s -> buffer.append(patchStr(s)).append("\r\n"));
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String patchStr = buffer.toString();
        if (patchDefault) {
            FileUtils.writeFile(langFile,patchStr);
        } else {
            FileUtils.writeFile(patchFile,patchStr);
        }

        afterPatch();
    }

    private static void afterPatch() {
        if (patchMap.size() != patchedList.size()) {
            System.out.println("Warning : Missing patch key from list : ");
            patchMap.forEach((key, value) -> {
                if (!patchedList.contains(key)) {
                    System.out.println(key);
                }
            });
            System.out.println("Check the PatchMap is Matched language File!");
        }

        String name = (String) config.get(FileUtils.PATCHED_TARGET_LANGUAGE_FILE);
        name = name.substring(0,name.length() - 4).split("csgo_")[1];
        System.out.println("Patch done.");
        System.out.println(patchDefault ? "you can launch game now." : String.format("Launch game with -language %s",name));
    }

    private static String patchStr(String str) {
        for (var entry : patchMap.entrySet()) {
            String key = entry.getKey();
            String value = replaceColorText(entry.getValue());

            str = str.trim();
            if (!str.startsWith("{") && !str.startsWith("}") && !str.startsWith("\"[english]")) {
                String[] s = str.split("\t");
                if (s[0].replaceAll("\"","").equalsIgnoreCase(key)) {
                    String fix = String.format("%s\t\"%s\"",s[0],value);
                    System.out.printf("Patch %s/%s : %s -> %s%n",count.getAndIncrement(),patchMap.size(),s[0],entry.getValue());
                    patchedList.add(key);
                    return fix;
                }
            }
        }
        return str;
    }

    public static String replaceColorText(String s) {
        @SuppressWarnings("unchecked")
        Map<String,String> syntaxConfig = (Map<String, String>) config.get(FileUtils.COLOR_SYNTAX);
        for (var entry : syntaxConfig.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            s = s.replaceAll(value,getColorCodeByKey(key));
        }
        return s;
    }

    private static String getColorCodeByKey(String s) {
        switch (s) {
                    case "Violet"      : return "\u000E";
                    case "Blue"        : return "\f";
                    case "Green(Money)": return "\u0006";
                    case "Light_Green" : return "\u0005";
                    case "CT_Color"    : return "\b";
                    case "Light_Blue"  : return "\u000B";
                    case "Red"         : return "\u0002";
                    case "Light_Red_2" : return "\u000F";
                    case "White"       : return "\u0001";
                    case "Team_Color"  : return "\u0003";
                    case "T_Color"     : return "\t";
                    case "Light_Red"   : return "\u0007";
                    case "Green"       : return "\u0004";
                    default            : return "";

            //Color Syntax
            // - White
            // - Red
            // - Team color (may cause player's color dot before symbol)
            // - Green
            // - Light green
            // - Green(money awards)
            // - Light red
            // - CT color
            //	 - T color
            // - Light blue
            // - Blue
            // - Violet
            // - Light red 2
        }
    }

    private static void checkFileExists() {
        String defaultLangFile = (String) config.get(FileUtils.DEFAULT_LANGUAGE_FILE);
        langFile = new File(".", defaultLangFile);
        if (!FileUtils.checkFileExist(langFile)) {
            System.out.println("ERROR: Default language file not exist! check Config file!");
            System.exit(-1);
        }

        if (patchDefault) {
            try {
                Files.copy(langFile.toPath(),new File(String.format("%s.backup",defaultLangFile)).toPath(), StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.COPY_ATTRIBUTES);
                System.out.println("Backup lang file success.");
            } catch (IOException e) {
                System.out.println("ERROR: Try backup lang file throw exception!");
                e.printStackTrace();
                System.exit(-1);
            }
        } else {
            patchFile = new File(".", (String) config.get(FileUtils.PATCHED_TARGET_LANGUAGE_FILE));
            if (!FileUtils.checkFileExist(patchFile)) {
                try {
                    if (!patchFile.createNewFile()) {
                        System.out.println("ERROR: Try create patchFile failed! Check permission!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
