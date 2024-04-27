package me.friwi.jcefmaven;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.friwi.jcefmaven.impl.platform.PlatformPatterns;

public enum EnumPlatform {
    		
    MACOSX_AMD64(PlatformPatterns.OS_MACOSX, PlatformPatterns.ARCH_AMD64, EnumOS.MACOSX),
    MACOSX_ARM64(PlatformPatterns.OS_MACOSX, PlatformPatterns.ARCH_ARM64, EnumOS.MACOSX),
    LINUX_AMD64(PlatformPatterns.OS_LINUX, PlatformPatterns.ARCH_AMD64, EnumOS.LINUX),
    LINUX_ARM64(PlatformPatterns.OS_LINUX, PlatformPatterns.ARCH_ARM64, EnumOS.LINUX),
    LINUX_ARM(PlatformPatterns.OS_LINUX, PlatformPatterns.ARCH_ARM, EnumOS.LINUX),
    WINDOWS_AMD64(PlatformPatterns.OS_WINDOWS, PlatformPatterns.ARCH_AMD64, EnumOS.WINDOWS),
    WINDOWS_I386(PlatformPatterns.OS_WINDOWS, PlatformPatterns.ARCH_I386, EnumOS.WINDOWS),
    WINDOWS_ARM64(PlatformPatterns.OS_WINDOWS, PlatformPatterns.ARCH_ARM64, EnumOS.WINDOWS);

    private final String[] osMatch;
    private final String[] archMatch;
    private final String identifier;
    private final EnumOS os;
    
    public static final String PROPERTY_OS_NAME = "os.name";
	public static final String PROPERTY_OS_ARCH = "os.arch";

    private static final Logger LOGGER = Logger.getLogger(EnumPlatform.class.getName());
    private static EnumPlatform DETECTED_PLATFORM = null;

    static {
        DETECTED_PLATFORM = null;
    }

    EnumPlatform(String[] osMatch, String[] archMatch, EnumOS os) {
        Objects.requireNonNull(osMatch, "osMatch cannot be null");
        Objects.requireNonNull(archMatch, "archMatch cannot be null");
        Objects.requireNonNull(os, "os cannot be null");
        this.osMatch = osMatch;
        this.archMatch = archMatch;
        this.identifier = name().toLowerCase(Locale.ENGLISH).replace("_", "-");
        this.os = os;
    }

    public static EnumPlatform getCurrentPlatform() throws UnsupportedPlatformException {
        if (DETECTED_PLATFORM != null) return DETECTED_PLATFORM;
        String osName = System.getProperty(PROPERTY_OS_NAME);
        String osArch = System.getProperty(PROPERTY_OS_ARCH);

        for (EnumPlatform platform : values()) {
            if (platform.matches(osName, osArch)) {
                DETECTED_PLATFORM = platform;
                return platform;
            }
        }

        String supported = "";
        for (EnumPlatform platform : values()) {
            supported = supported + platform.name() + "(" + PROPERTY_OS_NAME + ": " +
                        Arrays.toString((Object[]) platform.osMatch) + ", " + PROPERTY_OS_ARCH + ": " +
                        Arrays.toString((Object[]) platform.archMatch) + ")\n";
        }

        LOGGER.log(Level.SEVERE, "Can not detect your current platform. Is it supported?\n" +
                    "If you think that this is in error, please open an issue providing your " +
                    PROPERTY_OS_NAME + " and " + PROPERTY_OS_ARCH + " from below!\n\n" +
                    "Your platform specs:\n" + PROPERTY_OS_NAME + ": \"" + osName + "\"\n" +
                    PROPERTY_OS_ARCH + ": \"" + osArch + "\"\n\nSupported platforms:\n" + supported);

        throw new UnsupportedPlatformException(osName, osArch);
    }

    private boolean matches(String osName, String osArch) {
        Objects.requireNonNull(osName, "osName cannot be null");
        Objects.requireNonNull(osArch, "osArch cannot be null");

        boolean m = false;
        for (String os : this.osMatch) {
            if (osName.toLowerCase(Locale.ENGLISH).contains(os)) {
                m = true;
                break;
            }
        }
        if (!m) {
            return false;
        }

        for (String arch : this.archMatch) {
            if (osArch.toLowerCase(Locale.ENGLISH).equals(arch)) {
                return true;
            }
        }
        return false;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public EnumOS getOs() {
        return this.os;
    }
}
