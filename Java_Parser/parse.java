
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

class parse {
  public static void main(String[] args) {

    Path path = Paths.get("JMdict");
    Path path2 = Paths.get("JMdict.sql");
    Charset def = Charset.defaultCharset();
    Charset utf8 = Charset.forName("UTF-8");
    String SeqID = "";
    String pos = "";
    ArrayList<String> ReadingKanji = new ArrayList<String>();
    ArrayList<String> ReadingKana = new ArrayList<String>();

    ArrayList<String> english = new ArrayList<String>();
    int readingID = 0;
    int engID = 0;
    String keb = "";
    String reb = "";
    // String xref="";
    // Java 8, default UTF-8
    try (BufferedReader reader = Files.newBufferedReader(path)) {
      try (BufferedWriter writer = Files.newBufferedWriter(path2, StandardCharsets.UTF_8)) {
        PrintStream printStream = new PrintStream(System.out, true, utf8.name());
        String str;
        String outString = "";
        while ((str = reader.readLine()) != null) {

          if (str.startsWith("<entry>")) {
            while (!(str = reader.readLine()).startsWith("</entry>")) {
              if (str.startsWith("<ent_seq>"))
                SeqID = str.substring(str.indexOf(">") + 1, str.indexOf("</"));
              if (str.startsWith("<keb>")) {
                ReadingKanji.add(str.substring(str.indexOf(">") + 1, str.indexOf("</")));
                keb = str.substring(str.indexOf(">") + 1, str.indexOf("</"));
              }
              if (str.startsWith("<reb>")) {
                ReadingKana.add(str.substring(str.indexOf(">") + 1, str.indexOf("</")));
                reb = str.substring(str.indexOf(">") + 1, str.indexOf("</"));
              }
              if (str.startsWith("<pos>"))
                pos = str.substring(str.indexOf(">&") + 2, str.indexOf(";</"));
              if (str.startsWith("<gloss>"))
                english.add(str.substring(str.indexOf(">") + 1, str.indexOf("</")));
            }
            if (ReadingKana.size() == 0)
              ReadingKana.add("");
            if (ReadingKanji.size() == 0)
              ReadingKanji.add("");
            // printStream.printf("%s: \t %s \t: %s \t %s (%s)\n", SeqID,
            // ReadingKanji.firstElement(),
            // ReadingKana.firstElement(), english, pos);
            outString = String.format("insert into words values (%s,'A','%s');\n", SeqID, pos);
            while (!ReadingKanji.isEmpty() || !ReadingKana.isEmpty()) {
              if (ReadingKanji.isEmpty())
                ReadingKanji.add(keb);
              if (ReadingKana.isEmpty())
                ReadingKana.add(reb);
              outString = outString
                  .concat(String.format("insert into reading values (%s,%d,'%s','%s');\n", SeqID, readingID,
                      ReadingKanji.remove(0), ReadingKana.remove(0)));
            }
            while (!english.isEmpty()) {

              outString = outString
                  .concat(String.format("insert into frenglish values ('%s',%d);\n", english.remove(0), engID));
              outString = outString.concat(String.format("insert into frenglishref values (%d,%s);\n", engID,SeqID));
              engID++;
            }
          }
          writer.append(outString);
          SeqID = null;
          ReadingKana.clear();
          ReadingKanji.clear();
          pos = null;
          english.clear();
          readingID++;
          keb = "null";
          reb = "null";
          // Thread.sleep(500);

        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
