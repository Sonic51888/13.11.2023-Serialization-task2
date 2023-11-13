import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress game1 = new GameProgress(83, 4, 5, 40);
        GameProgress game2 = new GameProgress(50, 6, 3, 23);
        GameProgress game3 = new GameProgress(99, 8, 9, 100);
        saveGame("C:/Games/savegames/game1.dat", game1);
        saveGame("C:/Games/savegames/game2.dat", game2);
        saveGame("C:/Games/savegames/game3.dat", game3);
        List<String> listFiles = new ArrayList<>();
        File dir = new File("C:/Games/savegames/");
        // если объект представляет каталог
        if (dir.isDirectory()) {
            // получаем все вложенные объекты в каталоге
            for (File item : dir.listFiles()) {
                // проверим, является ли объект файлом
                if (item.isFile()) {
                    listFiles.add(item.getPath());
                }
            }
        }
        zipFiles("C:/Games/savegames/save.zip", listFiles);

    }

    public static boolean saveGame(String path, GameProgress game) {

        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    public static boolean zipFiles(String pathZip, List<String> listFiles) {
        try (ZipOutputStream zout = new ZipOutputStream(new
                FileOutputStream(pathZip))) {
            for (String path : listFiles) {
                try (FileInputStream fis = new FileInputStream(path)) {
                    ZipEntry entry = new ZipEntry(path);
                    zout.putNextEntry(entry);
                    // считываем содержимое файла в массив byte
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    // добавляем содержимое к архиву
                    zout.write(buffer);
                    // закрываем текущую запись для новой записи
                    zout.closeEntry();
                } catch (Exception ex) {
                    System.out.println(ex);
                    return false;
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        for (String path : listFiles) {
            File fileDel = new File(path);
            String s = fileDel.getPath().toString().replace("\\", "/");
            if (!s.equals(pathZip)) {
                fileDel.delete();
            }
        }
        return true;
    }
}