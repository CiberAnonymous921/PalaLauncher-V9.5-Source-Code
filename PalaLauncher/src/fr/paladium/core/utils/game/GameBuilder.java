/*    */ package fr.paladium.core.utils.game;
/*    */ import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
/*    */ import java.util.List;

/*    */ 
/*    */ import fr.paladium.core.distribution.GameDistribution;
import fr.paladium.core.utils.net.Telemetry;
/*    */ 
/*    */ public class GameBuilder {
/*    */   private String javaCommand;
/*    */   private String mainClass;
/*    */   private String classPath;
/*    */   private String[] vmArgs;
/*    */   private String[] args;
/*    */   private List<String> launcherArgs;
/*    */   private File rootDir;
/*    */   
/* 16 */   public void setJavaCommand(String javaCommand) { this.javaCommand = javaCommand; } public void setMainClass(String mainClass) { this.mainClass = mainClass; } public void setClassPath(String classPath) { this.classPath = classPath; } public void setVmArgs(String[] vmArgs) { this.vmArgs = vmArgs; } public void setArgs(String[] args) { this.args = args; } public void setLauncherArgs(List<String> launcherArgs) { this.launcherArgs = launcherArgs; } public void setRootDir(File rootDir) { this.rootDir = rootDir; }
/* 17 */   GameBuilder(String javaCommand, String mainClass, String classPath, String[] vmArgs, String[] args, List<String> launcherArgs, File rootDir) { this.javaCommand = javaCommand; this.mainClass = mainClass; this.classPath = classPath; this.vmArgs = vmArgs; this.args = args; this.launcherArgs = launcherArgs; this.rootDir = rootDir; } public static GameBuilderBuilder builder() { return new GameBuilderBuilder(); } public static class GameBuilderBuilder { private String javaCommand; private String mainClass; private String classPath; public GameBuilderBuilder javaCommand(String javaCommand) { this.javaCommand = javaCommand; return this; } private String[] vmArgs; private String[] args; private List<String> launcherArgs; private File rootDir; public GameBuilderBuilder mainClass(String mainClass) { this.mainClass = mainClass; return this; } public GameBuilderBuilder classPath(String classPath) { this.classPath = classPath; return this; } public GameBuilderBuilder vmArgs(String[] vmArgs) { this.vmArgs = vmArgs; return this; } public GameBuilderBuilder args(String[] args) { this.args = args; return this; } public GameBuilderBuilder launcherArgs(List<String> launcherArgs) { this.launcherArgs = launcherArgs; return this; } public GameBuilderBuilder rootDir(File rootDir) { this.rootDir = rootDir; return this; } public GameBuilder build() { return new GameBuilder(this.javaCommand, this.mainClass, this.classPath, this.vmArgs, this.args, this.launcherArgs, this.rootDir); } public String toString() { return "GameBuilder.GameBuilderBuilder(javaCommand=" + this.javaCommand + ", mainClass=" + this.mainClass + ", classPath=" + this.classPath + ", vmArgs=" + Arrays.deepToString((Object[])this.vmArgs) + ", args=" + Arrays.deepToString((Object[])this.args) + ", launcherArgs=" + this.launcherArgs + ", rootDir=" + this.rootDir + ")"; }
/*    */      }
/*    */   
/* 20 */   public String getJavaCommand() { return this.javaCommand; }
/* 21 */   public String getMainClass() { return this.mainClass; }
/* 22 */   public String getClassPath() { return this.classPath; }
/* 23 */   public String[] getVmArgs() { return this.vmArgs; }
/* 24 */   public String[] getArgs() { return this.args; }
/* 25 */   public List<String> getLauncherArgs() { return this.launcherArgs; } public File getRootDir() {
/* 26 */     return this.rootDir;
/*    */   }
/*    */   public static GameBuilder build(GameDistribution distribution) {
/* 29 */     return builder()
/* 30 */       .javaCommand(distribution.getJava())
/* 31 */       .mainClass(distribution.getMain())
/* 32 */       .classPath(String.join(String.valueOf(File.pathSeparatorChar), distribution.getClasspath()))
/* 33 */       .vmArgs(distribution.getArguments().getJvm())
/* 34 */       .args(distribution.getArguments().getGame())
/* 35 */       .launcherArgs(new ArrayList<>())
/* 36 */       .rootDir(distribution.getRootDir())
/* 37 */       .build();
/*    */   }
/*    */   
/*    */   public Process run(boolean quotePath) throws IOException {
/* 41 */     ProcessBuilder builder = new ProcessBuilder(new String[0]);
/* 42 */     builder.environment().remove("_JAVA_OPTIONS");
/* 43 */     builder.redirectErrorStream(true);
/* 44 */     builder.directory(this.rootDir);
/*    */     
/* 46 */     List<String> commands = new ArrayList<>();
/* 47 */     if (quotePath) {
/* 48 */       commands.add("\"" + this.javaCommand + "\"");
/*    */     } else {
/* 50 */       commands.add(this.javaCommand);
/*    */     } 
/* 52 */     commands.addAll(Arrays.asList(this.vmArgs));
/* 53 */     commands.add("-cp");
/* 54 */     if (quotePath) {
/* 55 */       commands.add("\"" + this.classPath + "\"");
/*    */     } else {
/* 57 */       commands.add(this.classPath);
/*    */     } 
/* 59 */     commands.add(this.mainClass);
/* 60 */     commands.addAll(Arrays.asList(this.args));
/* 61 */     commands.addAll(this.launcherArgs);
/*    */     
/* 63 */     List<String> censuredCommands = new ArrayList<>(commands);
/* 64 */     for (int i = 0; i < censuredCommands.size(); i++) {
/* 65 */       String command = censuredCommands.get(i);
/* 66 */       if (command.contains("--accessToken")) {
/* 67 */         censuredCommands.set(i + 1, "***");
/*    */       }
/*    */     } 
/* 70 */     System.out.println("Running command: " + String.join(" ", (Iterable)censuredCommands));
/* 71 */     Telemetry.collect(Telemetry.Type.INFO_LAUNCH);
/*    */     
/* 73 */     return builder.command(commands).start();
/*    */   }
/*    */ }


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\cor\\utils\game\GameBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */