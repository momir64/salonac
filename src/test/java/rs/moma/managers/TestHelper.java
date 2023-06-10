package rs.moma.managers;

import java.nio.file.Files;
import java.nio.file.Paths;

import static rs.moma.helper.DataTools.*;

public class TestHelper {
    public static void ClearDatabase() throws Exception {
        Files.createDirectories(Paths.get(fileFolder));
        Files.write(Paths.get(fileZakazaniTretmani), "".getBytes());
        Files.write(Paths.get(fileTipoviTretmana), "".getBytes());
        Files.write(Paths.get(fileZaposleni), "".getBytes());
        Files.write(Paths.get(fileTretmani), "".getBytes());
        Files.write(Paths.get(fileKlijenti), "".getBytes());
        Files.write(Paths.get(fileIsplate), "".getBytes());
        Files.write(Paths.get(fileSalon), "".getBytes());
    }
}
