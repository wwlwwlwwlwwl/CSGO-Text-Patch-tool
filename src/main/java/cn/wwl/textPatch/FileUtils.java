package cn.wwl.textPatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    public static final File CONFIG_FILE = new File(".","TextPatch_Config.json");
    public static final File PATCH_MAP = new File(".","TextPatch_Map.json");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public static final String DEFAULT_LANGUAGE_FILE = "Default_Lang_File";
    public static final String PATCHED_TARGET_LANGUAGE_FILE = "Patched_Lang_File";
    public static final String PATCH_DEFAULT_LANGUAGE_FILE = "Patch_Default_LangFile";
    public static final String COLOR_SYNTAX = "Color_Syntax";

    public static Map<String,Object> getConfig() {
        try {
            return GSON.fromJson(new FileReader(CONFIG_FILE, StandardCharsets.UTF_8),Map.class);
        } catch (IOException e) {
            System.out.println("ERROR: Throw exception in read Config file.");
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String,String> getPatchMap() {
        try {
            return GSON.fromJson(new FileReader(PATCH_MAP, StandardCharsets.UTF_8),Map.class);
        } catch (IOException e) {
            System.out.println("ERROR: Throw exception in read PatchMap file.");
            e.printStackTrace();
        }
        return null;
    }

    public static void checkAndInitConfig() {
        if (!checkFileExist(CONFIG_FILE)) {
            try {
                writeToFile(CONFIG_FILE, makeEmptyConfig());
                System.out.println("Write default config to Config file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!checkFileExist(PATCH_MAP)) {
            try {
                writeToFile(PATCH_MAP,makePatchMapTemplate());
                System.out.println("Write default config to Patch map.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String makePatchMapTemplate() {
        Map<String, String> template = new HashMap<>();
        //dangerZone Rank
        template.put("skillgroup_1dangerzone", "(1/15) ????????? ??? ???");
        template.put("skillgroup_2dangerzone", "(2/15) ????????? ??? ???");
        template.put("skillgroup_3dangerzone", "(3/15) ?????? ??? ???");
        template.put("skillgroup_4dangerzone", "(4/15) ?????? ??? ???");
        template.put("skillgroup_5dangerzone", "(5/15) ?????? ??? ???");
        template.put("skillgroup_6dangerzone", "(6/15) ?????? ??? ???");
        template.put("skillgroup_7dangerzone", "(7/15) ?????? ??? ??????");
        template.put("skillgroup_8dangerzone", "(8/15) ?????? ??? ???");
        template.put("skillgroup_9dangerzone", "(9/15) ?????? ??? ???");
        template.put("skillgroup_10dangerzone","(10/15) ??????  ??? ???");
        template.put("skillgroup_11dangerzone","(11/15) ?????? ??? ??????");
        template.put("skillgroup_12dangerzone","(12/15) ?????????");
        template.put("skillgroup_13dangerzone","(13/15) ????????????");
        template.put("skillgroup_14dangerzone","(14/15) ????????????");
        template.put("skillgroup_15dangerzone","(15/15) ???????????????");

        //Rank
        template.put("skillgroup_1"	,"(1/18) ?????? ??? I");
        template.put("skillgroup_2"	,"(2/18) ?????? ??? II");
        template.put("skillgroup_3"	,"(3/18) ?????? ??? III");
        template.put("skillgroup_4"	,"(4/18) ?????? ??? IV");
        template.put("skillgroup_5"	,"(5/18) ?????? ??? ??????");
        template.put("skillgroup_6"	,"(6/18) ?????? ??? ???????????????");
        template.put("skillgroup_7"	,"(7/18) ???????????? ??? I");
        template.put("skillgroup_8"	,"(8/18) ???????????? ??? II");
        template.put("skillgroup_9"	,"(9/18) ???????????? ??? III");
        template.put("skillgroup_10","(10/18) ???????????? ??? ?????????");
        template.put("skillgroup_11","(11/18) ?????????????????? ??? I");
        template.put("skillgroup_12","(12/18) ?????????????????? ??? II");
        template.put("skillgroup_13","(13/18) ?????????????????? ??? ??????");
        template.put("skillgroup_14","(14/18) ?????????????????? ??? ??????");
        template.put("skillgroup_15","(15/18) ????????????");
        template.put("skillgroup_16","(16/18) ???????????? ??? ?????????");
        template.put("skillgroup_17","(17/18) ?????????????????????");
        template.put("skillgroup_18","(18/18) ????????????");
        //float
        template.put("SFUI_InvTooltip_Wear_Amount_0","(1/5) ????????????");
        template.put("SFUI_InvTooltip_Wear_Amount_1","(2/5) ????????????");
        template.put("SFUI_InvTooltip_Wear_Amount_2","(3/5) ????????????");
        template.put("SFUI_InvTooltip_Wear_Amount_3","(4/5) ????????????");
        template.put("SFUI_InvTooltip_Wear_Amount_4","(5/5) ????????????");
        return GSON.toJson(template);
    }

    private static String makeEmptyConfig() {
        Map<String, Object> config = new HashMap<>();
        Map<String, String> syntaxConfig = new HashMap<>();
        config.put(DEFAULT_LANGUAGE_FILE,"csgo/resource/csgo_schinese.txt");
        config.put(PATCHED_TARGET_LANGUAGE_FILE,"csgo/resource/csgo_cnPatch.txt");
        config.put(PATCH_DEFAULT_LANGUAGE_FILE,true);
        config.put(COLOR_SYNTAX,syntaxConfig);

        syntaxConfig.put("White",":white:");
        syntaxConfig.put("Red",":red:");
        syntaxConfig.put("Team_Color",":team:");
        syntaxConfig.put("Green",":green:");
        syntaxConfig.put("Light_Green",":light_green:");
        syntaxConfig.put("Green(Money)",":green_money:");
        syntaxConfig.put("Light_Red",":light_red:");
        syntaxConfig.put("CT_Color",":ct:");
        syntaxConfig.put("T_Color",":t:");
        syntaxConfig.put("Light_Blue",":light_blue:");
        syntaxConfig.put("Blue",":blue:");
        syntaxConfig.put("Violet",":violet:");
        syntaxConfig.put("Light_Red_2",":light_red2:");

        return GSON.toJson(config);

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

    private static void writeToFile(File file,String data) throws IOException {
        if (!file.exists()) {
            if (file.createNewFile()) {
                System.out.printf("Config file %s not exist, Created.%n", file.getAbsolutePath());
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(data);
        writer.flush();
        writer.close();
    }

    public static boolean checkFileExist(File file) {
        return file != null && file.exists();
    }

    public static BufferedReader createFileReader(File file) {
        if (!checkFileExist(file)) {
            return null;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_16LE));
            return reader;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(File file,String str) {
        if (!checkFileExist(file)) {
            return;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file,StandardCharsets.UTF_16LE));
            writer.write(str);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
