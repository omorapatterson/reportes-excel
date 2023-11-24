package catalogo.reportes.core.imagenes.filemanager;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MyFileManager {

    Logger logger = LogManager.getLogger(MyFileManager.class);

    public MyFileManager() {
    }

    public String readFromInputStream(final InputStream inputStream) throws IOException {
        final StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public void writeFile(final String filename, final String text) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
            fos.write(text.getBytes("UTF-8"));
            close(fos);
        } catch (final IOException e) {
            close(fos);
            logger.log(Level.ERROR,"Se produjo un error guardando la última fecha de actualización: " + e.getStackTrace());
            throw new IOException("Se produjo un error guardando la última fecha de actualización: " + e.getStackTrace());
        }

    }

    private void close(final Closeable closeable) {
        try {
            closeable.close();
        } catch (final IOException ignored) {
            logger.log(Level.ERROR,"Ocurrio un problema a cerrar el fichero", ignored);
        }
    }

}