package com.h3110w0r1d.burploaderkeygen;

import com.h3110w0r1d.json.JSONObject;
import com.h3110w0r1d.json.JSONParse;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Properties;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class KeygenForm {
   private static final String Version = "v1.17";
   private static JFrame frame;
   private static JButton btn_run;
   private static JTextField text_cmd;
   private static JTextField text_license_text;
   private static JTextArea text_license;
   private static JTextArea request;
   private static JTextArea response;
   private static JLabel label0_1;
   private static JPanel panel1;
   private static JPanel panel2;
   private static JPanel panel3;
   private static JCheckBox check_autorun;
   private static JCheckBox check_ignore;
   private static JCheckBox check_early;
   private static String LatestVersion;
   private static final String DownloadURL = "https://portswigger-cdn.net/burp/releases/download?product=pro&type=Jar&version=";
   private static final String LoaderPath;
   private static String VersionData;
   private static final String LoaderDir;
   private static final String ConfigFileName;
   private static String[] cmd;
   private static String cmd_str;

   public static BufferedReader execCommand(String[] command) {
      try {
         Process proc = new ProcessBuilder(command).start();
         return new BufferedReader(new InputStreamReader(proc.getErrorStream()));
      } catch (Exception var2) {
         return null;
      }
   }

   private static int getJavaVersion(String path) {
      String[] command = new String[]{path, "-version"};
      BufferedReader buf = execCommand(command);
      if (buf == null) {
         return 0;
      } else {
         String line;
         do {
            try {
               line = buf.readLine();
            } catch (IOException var5) {
               return 0;
            }

            if (line == null) {
               System.out.println("Warning: cannot get Java version of '" + path + "'!");
               return 0;
            }
         } while (!line.contains("version"));

         String[] version = line.split("\"")[1].split("[.\\-]");
         return "1".equals(version[0]) ? Integer.parseInt(version[1]) : Integer.parseInt(version[0]);
      }
   }

   private static String getBurpPath() {
      String newest_file = "burpsuite_jar_not_found";

      try {
         long newest_time = 0L;
         File f = new File(LoaderPath);
         String current_dir;
         if (f.isDirectory()) {
            current_dir = f.getPath();
         } else {
            current_dir = f.getParentFile().toString();
         }

         DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(current_dir), "burpsuite_*.jar");

         for (Path path : dirStream) {
            if (!Files.isDirectory(path) && newest_time < path.toFile().lastModified()) {
               newest_time = path.toFile().lastModified();
               newest_file = path.toString();
            }
         }

         dirStream.close();
      } catch (Throwable var8) {
      }

      return newest_file;
   }

   private static boolean IsWindows() {
      return System.getProperty("os.name").toLowerCase().startsWith("win");
   }

   private static String[] GetCMD() {
      String JAVA_PATH = getJavaPath();
      int JAVA_VERSION = getJavaVersion(JAVA_PATH);
      String BURP_PATH = getBurpPath();
      ArrayList<String> cmd = new ArrayList<>();
      cmd.add(JAVA_PATH);
      if (JAVA_VERSION == 0) {
         return new String[]{"Cannot find java! Please put jdk in the same path with keygen."};
      } else {
         if (JAVA_VERSION == 16) {
            cmd.add("--illegal-access=permit");
         }

         if (JAVA_VERSION >= 17) {
            cmd.add("--add-opens=java.desktop/javax.swing=ALL-UNNAMED");
            cmd.add("--add-opens=java.base/java.lang=ALL-UNNAMED");
            cmd.add("--add-opens=java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED");
            cmd.add("--add-opens=java.base/jdk.internal.org.objectweb.asm.tree=ALL-UNNAMED");
            cmd.add("--add-opens=java.base/jdk.internal.org.objectweb.asm.Opcodes=ALL-UNNAMED");
         }

         if (JAVA_VERSION > 8) {
            cmd.add("-javaagent:" + LoaderPath);
            cmd.add("-noverify");
            cmd.add("-jar");
            cmd.add(BURP_PATH);
            return cmd.toArray(new String[0]);
         } else {
            return new String[]{"Not support Java 8, please use old version! https://github.com/h3110w0r1d-y/BurpLoaderKeygen/releases/tag/1.7"};
         }
      }
   }

   private static String GetCMDStr(String[] cmd) {
      StringBuilder cmd_str = new StringBuilder();

      for (String x : cmd) {
         cmd_str.append("\"").append(x.replace("\"", "\\\"")).append("\" ");
      }

      return cmd_str.toString();
   }

   private static boolean verifyFile(File javafile) {
      if (!javafile.exists() || javafile.isDirectory()) {
         return false;
      } else if (!javafile.canExecute()) {
         System.out.println("Warning: '" + javafile.getPath() + "' can not execute!");
         return false;
      } else {
         System.out.println("\u001b[32mSuccess\u001b[0m: '" + javafile.getPath() + "' can execute!");
         return true;
      }
   }

   private static String verifyPath(String path) {
      File javafile = new File(path);
      if (verifyFile(javafile)) {
         return javafile.getPath();
      } else {
         javafile = new File(path + ".exe");
         return verifyFile(javafile) ? javafile.getPath() : null;
      }
   }

   private static String getJavaPath() {
      String[] paths = new String[]{
         LoaderDir + File.separator + "bin",
         LoaderDir + File.separator + "jre" + File.separator + "bin",
         LoaderDir + File.separator + "jdk" + File.separator + "bin",
         System.getProperty("java.home") + File.separator + "bin"
      };
      String java_path = null;

      for (String path_str : paths) {
         java_path = verifyPath(path_str + File.separator + "java");
         if (java_path != null) {
            break;
         }

         try {
            for (Path path : Files.newDirectoryStream(Paths.get(path_str), "java[0-9]{1,2}")) {
               if (!Files.isDirectory(path) && Files.isExecutable(path)) {
                  return path.toString();
               }
            }

            for (Path pathx : Files.newDirectoryStream(Paths.get(path_str), "java[0-9]{1,2}\\.exe")) {
               if (!Files.isDirectory(pathx) && Files.isExecutable(pathx)) {
                  return pathx.toString();
               }
            }
         } catch (IOException var9) {
         }
      }

      return java_path;
   }

   private static String readProperty(String key) {
      Properties properties = new Properties();
      File file = new File(ConfigFileName);

      try {
         file.createNewFile();
      } catch (Exception var6) {
         return "0";
      }

      try {
         InputStream inStream = Files.newInputStream(file.toPath());
         properties.load(inStream);
      } catch (IOException var5) {
         return "0";
      }

      return properties.getProperty(key);
   }

   private static void setProperty(String key, String value) {
      Properties properties = new Properties();

      try {
         InputStream inStream = Files.newInputStream(Paths.get(ConfigFileName));
         properties.load(inStream);
         properties.setProperty(key, value);
         FileOutputStream out = new FileOutputStream(ConfigFileName, false);
         properties.store(out, "");
         out.close();
      } catch (IOException var6) {
      }
   }

   public static String GetHTTPBody(String url) {
      try {
         URL realUrl = new URL(url);
         HttpsURLConnection https = (HttpsURLConnection)realUrl.openConnection();
         https.connect();
         if (https.getResponseCode() == 200) {
            InputStream is = https.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sbf = new StringBuilder();

            String temp;
            while ((temp = br.readLine()) != null) {
               sbf.append(temp);
               sbf.append("\r\n");
            }

            return sbf.toString();
         }
      } catch (Exception var7) {
      }

      return "";
   }

   private static void trustAllHosts() {
      TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
         @Override
         public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
         }

         @Override
         public void checkClientTrusted(X509Certificate[] chain, String authType) {
         }

         @Override
         public void checkServerTrusted(X509Certificate[] chain, String authType) {
         }
      }};

      try {
         SSLContext sc = SSLContext.getInstance("TLS");
         sc.init(null, trustAllCerts, new SecureRandom());
         HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   private static String GetLatestVersion() {
      try {
         if (VersionData == null) {
            VersionData = GetHTTPBody("https://portswigger.net/burp/releases/data?pageSize=5");
         }

         JSONObject data = JSONParse.Parse(VersionData);
         if (data == null) {
            return "";
         }

         JSONObject[] Results = data.get("ResultSet").getList("Results");

         for (JSONObject Result : Results) {
            boolean isProfessional = false;
            boolean isEarlyAdopter = false;

            for (JSONObject category : Result.getList("categories")) {
               if ("Professional".equals(category.String())) {
                  isProfessional = true;
               }
            }

            if (isProfessional) {
               if ("Early Adopter".equals(Result.getList("releaseChannels")[0].String())) {
                  isEarlyAdopter = true;
               }

               if (!isEarlyAdopter || "1".equals(readProperty("early"))) {
                  return Result.getString("version");
               }
            }
         }
      } catch (Exception var12) {
      }

      return "";
   }

   private static void CheckNewVersion() {
      label0_1.setText("Checking the latest version of BurpSuite...");
      label0_1.setForeground(Color.BLACK);
      LatestVersion = GetLatestVersion();
      if (LatestVersion.equals("")) {
         label0_1.setText("Failed to check the latest version of BurpSuite");
      } else if (!cmd_str.contains(LatestVersion + ".jar")) {
         label0_1.setText("Latest version: " + LatestVersion + ". Click to download.");
         label0_1.setForeground(Color.BLUE);
      } else {
         label0_1.setText("Your BurpSuite is already the latest version(" + LatestVersion + ")");
         label0_1.setForeground(new Color(0, 100, 0));
      }
   }

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         trustAllHosts();
      } catch (Exception var5) {
      }

      String LicenseName = "h3110w0r1d";
      if (readProperty("auto_run") == null) {
         setProperty("auto_run", "0");
      }

      if (readProperty("ignore") == null) {
         setProperty("ignore", "0");
      }

      if (readProperty("early") == null) {
         setProperty("early", "0");
      }

      for (int i = 0; i < args.length; i++) {
         String label2 = args[i];
         switch (label2) {
            case "-a":
            case "-auto":
               if (i + 1 < args.length) {
                  if (args[i + 1].equals("0")) {
                     setProperty("auto_run", "0");
                  } else {
                     setProperty("auto_run", "1");
                  }
               } else {
                  setProperty("auto_run", "1");
               }
               break;
            case "-i":
            case "-ignore":
               if (i + 1 < args.length) {
                  if (args[i + 1].equals("0")) {
                     setProperty("ignore", "0");
                  } else {
                     setProperty("ignore", "1");
                  }
               } else {
                  setProperty("ignore", "1");
               }
               break;
            case "-n":
            case "-name":
               if (i + 1 < args.length) {
                  LicenseName = args[i + 1];
               }
         }
      }

      if (cmd_str.endsWith(".jar\" ") && readProperty("auto_run").equals("1")) {
         try {
            new ProcessBuilder(cmd).start();
            if (readProperty("ignore").equals("1") || cmd_str.contains(GetLatestVersion() + ".jar")) {
               System.exit(0);
            }
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }

      panel1 = new JPanel();
      panel2 = new JPanel();
      panel3 = new JPanel();
      frame = new JFrame("Burp Suite Pro Loader & Keygen v1.17 - By h3110w0r1d");
      btn_run = new JButton("Run");
      label0_1 = new JLabel("Checking the latest version of BurpSuite...");
      JLabel label1 = new JLabel("Loader Command:", 4);
      JLabel label2 = new JLabel("License Text:", 4);
      text_cmd = new JTextField(cmd_str);
      text_license_text = new JTextField("licensed to " + LicenseName);
      text_license = new JTextArea(Keygen.generateLicense(text_license_text.getText()));
      request = new JTextArea();
      response = new JTextArea();
      check_autorun = new JCheckBox("Auto Run");
      check_ignore = new JCheckBox("Ignore Update");
      check_early = new JCheckBox("Early Adopter");
      check_autorun.setBounds(150, 25, 110, 20);
      check_autorun.setSelected(readProperty("auto_run").equals("1"));
      check_autorun.addChangeListener(changeEvent -> {
         if (check_autorun.isSelected()) {
            setProperty("auto_run", "1");
         } else {
            setProperty("auto_run", "0");
         }
      });
      check_ignore.setBounds(260, 25, 150, 20);
      check_ignore.setSelected(readProperty("ignore").equals("1"));
      check_ignore.addChangeListener(changeEvent -> {
         if (check_ignore.isSelected()) {
            setProperty("ignore", "1");
         } else {
            setProperty("ignore", "0");
         }
      });
      check_early.setBounds(410, 25, 150, 20);
      check_early.setSelected(readProperty("early").equals("1"));
      check_early.addChangeListener(changeEvent -> {
         if (check_early.isSelected()) {
            setProperty("early", "1");
         } else {
            setProperty("early", "0");
         }

         CheckNewVersion();
      });
      label0_1.setLocation(150, 5);
      label1.setBounds(5, 50, 140, 22);
      text_cmd.setLocation(150, 50);
      btn_run.setSize(60, 22);
      label2.setBounds(5, 77, 140, 22);
      text_license_text.setLocation(150, 77);
      panel1.setBorder(BorderFactory.createTitledBorder("License"));
      panel2.setBorder(BorderFactory.createTitledBorder("Activation Request"));
      panel3.setBorder(BorderFactory.createTitledBorder("Activation Response"));
      text_license.setLocation(10, 15);
      request.setLocation(10, 15);
      response.setLocation(10, 15);
      panel1.setLayout(null);
      panel2.setLayout(null);
      panel3.setLayout(null);
      frame.setLayout(null);
      frame.setMinimumSize(new Dimension(800, 500));
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(3);
      frame.setBackground(Color.LIGHT_GRAY);
      frame.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            int H = KeygenForm.frame.getHeight() - 150;
            int W = KeygenForm.frame.getWidth();
            KeygenForm.text_cmd.setSize(W - 235, 22);
            KeygenForm.btn_run.setLocation(W - 80, 50);
            KeygenForm.text_license_text.setSize(W - 170, 22);
            KeygenForm.label0_1.setSize(W - 170, 20);
            KeygenForm.text_license.setSize((W - 15) / 2 - 25, H / 2 - 25);
            KeygenForm.request.setSize((W - 15) / 2 - 25, H / 2 - 25);
            KeygenForm.response.setSize(W - 43, H / 2 - 25);
            KeygenForm.panel1.setBounds(5, 104, (W - 15) / 2 - 5, H / 2);
            KeygenForm.panel2.setBounds(3 + (W - 15) / 2, 104, (W - 15) / 2 - 5, H / 2);
            KeygenForm.panel3.setBounds(5, 109 + H / 2, W - 23, H / 2);
         }
      });
      btn_run.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            try {
               Runtime.getRuntime().exec(KeygenForm.GetCMD());
            } catch (IOException var3) {
               var3.printStackTrace();
            }
         }
      });
      text_license.setLineWrap(true);
      text_license.setEditable(false);
      text_license_text.setHorizontalAlignment(0);
      text_license.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
      text_license_text.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            KeygenForm.text_license.setText(Keygen.generateLicense(KeygenForm.text_license_text.getText()));
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            KeygenForm.text_license.setText(Keygen.generateLicense(KeygenForm.text_license_text.getText()));
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            KeygenForm.text_license.setText(Keygen.generateLicense(KeygenForm.text_license_text.getText()));
         }
      });
      request.setLineWrap(true);
      request.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
      request.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            KeygenForm.response.setText(Keygen.generateActivation(KeygenForm.request.getText()));
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            KeygenForm.response.setText(Keygen.generateActivation(KeygenForm.request.getText()));
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            KeygenForm.response.setText(Keygen.generateActivation(KeygenForm.request.getText()));
         }
      });
      response.setLineWrap(true);
      response.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
      frame.add(check_autorun);
      frame.add(check_ignore);
      frame.add(check_early);
      frame.add(btn_run);
      frame.add(label0_1);
      frame.add(label1);
      frame.add(label2);
      frame.add(panel1);
      frame.add(panel2);
      frame.add(panel3);
      frame.add(text_cmd);
      frame.add(text_license_text);
      panel1.add(text_license);
      panel2.add(request);
      panel3.add(response);
      if (text_cmd.getText().contains("burpsuite_jar_not_found.jar")) {
         btn_run.setEnabled(false);
         check_autorun.setSelected(false);
         check_autorun.setEnabled(false);
      }

      frame.setVisible(true);
      btn_run.setFocusable(false);
      LatestVersion = GetLatestVersion();
      if (LatestVersion.equals("")) {
         label0_1.setText("Failed to check the latest version of BurpSuite");
      } else if (!cmd_str.contains(LatestVersion + ".jar")) {
         label0_1.setText("Latest version: " + LatestVersion + ". Click to download.");
         label0_1.setForeground(Color.BLUE);
         label0_1.setCursor(Cursor.getPredefinedCursor(12));
         label0_1.addMouseListener(
            new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent e) {
                  super.mouseClicked(e);

                  try {
                     Desktop.getDesktop()
                        .browse(new URI("https://portswigger-cdn.net/burp/releases/download?product=pro&type=Jar&version=" + KeygenForm.LatestVersion));
                  } catch (Exception var3) {
                  }
               }
            }
         );
      } else {
         label0_1.setText("Your BurpSuite is already the latest version(" + LatestVersion + ")");
         label0_1.setForeground(new Color(0, 100, 0));
      }
   }

   static {
      try {
         if (IsWindows()) {
            LoaderPath = KeygenForm.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().substring(1);
         } else {
            LoaderPath = KeygenForm.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
         }
      } catch (URISyntaxException var1) {
         throw new RuntimeException(var1);
      }

      LoaderDir = new File(LoaderPath).getParent();
      ConfigFileName = LoaderDir + File.separator + ".config.ini";
      cmd = GetCMD();
      cmd_str = GetCMDStr(cmd);
   }
}
