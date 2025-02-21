package androidx.core.app;

public class MethodTools2
{
    public static native String method02(String paramString);

    static
    {
        System.loadLibrary("mediaLib");
    }
}
