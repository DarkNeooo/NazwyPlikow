package com.company;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String appPath = new File("").getAbsolutePath();
        String path = new String();
        Scanner key = new Scanner(System.in);
        int menuOption = 0;

        System.out.println("Witamy w narzędziu do zmiany nazw plików!");
        showMenu();

        do {

            try{
                menuOption = key.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Podaj numer opcji");
                key.next();
                continue;
            }

            switch (menuOption) {
                case 1:
                    changeFilenames(appPath);
                    break;
                case 2:
                    System.out.println("Podaj ścieżkę do folderu");
                    path = key.next();
                    if(validatePath(path)) {
                        changeFilenames(path);
                    }
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Nie ma więcej opcji");
                    break;
            }

        }while(menuOption != 3);
    }

    public static void showMenu(){
        System.out.println("Co chcesz zrobić?");
        System.out.println("1. Zmienić nazwy plików w folderze, w którym znajduje się aplikacja");
        System.out.println("2. Zmienić nazwy plików w innym folderze");
        System.out.println("3. Wyłączyć aplikację");
    }

    public static void changeFilenames(String path){
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        String name = getAppName();
        int changeCounter = 0;

        if(listOfFiles.length != 0) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {

                    Path file = listOfFiles[i].toPath();
                    if (!listOfFiles[i].getName().equals(name)) {

                        try {
                            DateFormat cstFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_");

                            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);

                            String prefix = cstFormat.format(attributes.lastModifiedTime().toMillis());

                            String newName = path + "\\" + prefix + listOfFiles[i].getName();
                            File renamedFile = new File(newName);
                            listOfFiles[i].renameTo(renamedFile);
                            FileWriter out = new FileWriter(renamedFile, true);
                            changeCounter++;


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }else{
            System.out.println("Wskazany folder jest pusty \n");
            showMenu();
        }
        if(changeCounter > 0){
            System.out.println("Zmieniono " + (changeCounter == 1 ? "nazwę " : "nazwy ")  + changeCounter + (changeCounter == 1 ? " pliku" : " plików" + "\n"));
            showMenu();
        }
    }

    public static boolean validatePath(String path){
        File file = new File(path);
        return (file.exists() && file.isDirectory());
    }

    public static String getAppName(){

        String name = new String();

        try {
            name = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        for(int i = name.length() - 1;  i >= 0; i-- ){

            if(name.charAt(i) == '\\'){
                return name.substring(i + 1);
            }

        }

        return name;
    }
}
