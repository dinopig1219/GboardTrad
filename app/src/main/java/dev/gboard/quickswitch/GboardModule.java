package dev.gboard.quickswitch;

import android.util.Log;

import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Diagnostic milestone only: confirms target classes and records their runtime
 * members. It deliberately installs no UI hooks until the exact Gboard contracts
 * have been verified, so an unknown Gboard release cannot destabilize the IME.
 */
public final class GboardModule extends XposedModule {
    private static final String TAG = "GboardQuickTradSimp";
    private static final String TARGET = "com.google.android.inputmethod.latin";

    private static final String[] CANDIDATES = {
            "com.google.android.libraries.inputmethod.accesspoint.widget.AccessPointsBar",
            "com.google.android.libraries.inputmethod.accesspoint.widget.AccessPointsBarItemSoftKeyView",
            "com.google.android.libraries.inputmethod.accesspoint.widget.AccessPointsPanel",
            "com.google.android.libraries.inputmethod.accesspoint.widget.AccessPointsPanelV2",
            "com.google.android.libraries.inputmethod.accesspoint.impl.AccessPointKeyboard",
            "com.google.android.libraries.inputmethod.accesspoint.impl.AccessPointsPanelMotionEventHandler",
            "com.google.android.apps.inputmethod.libs.chinese.ime.AsyncChineseProcessorBasedIme"
    };

    @Override
    public void onModuleLoaded(XposedModuleInterface.ModuleLoadedParam param) {
        log(Log.INFO, TAG, "Loaded; framework=" + getFrameworkName()
                + " " + getFrameworkVersion() + ", API=" + getApiVersion());
    }

    @Override
    public void onPackageReady(XposedModuleInterface.PackageReadyParam param) {
        if (!TARGET.equals(param.getPackageName())) return;

        ClassLoader loader = param.getClassLoader();
        log(Log.INFO, TAG, "Gboard process ready; classLoader=" + loader);
        for (String name : CANDIDATES) dumpClass(loader, name);
    }

    private void dumpClass(ClassLoader loader, String name) {
        try {
            Class<?> type = Class.forName(name, false, loader);
            log(Log.INFO, TAG, "FOUND " + name + " modifiers=" + type.getModifiers());

            Field[] fields = type.getDeclaredFields();
            Arrays.sort(fields, Comparator.comparing(Field::getName));
            for (Field field : fields) {
                log(Log.INFO, TAG, " FIELD " + field.toGenericString());
            }

            Constructor<?>[] constructors = type.getDeclaredConstructors();
            Arrays.sort(constructors, Comparator.comparing(Constructor::toGenericString));
            for (Constructor<?> constructor : constructors) {
                log(Log.INFO, TAG, " CTOR " + constructor.toGenericString());
            }

            Method[] methods = type.getDeclaredMethods();
            Arrays.sort(methods, Comparator.comparing(Method::toGenericString));
            for (Method method : methods) {
                log(Log.INFO, TAG, " METHOD " + method.toGenericString());
            }
        } catch (ClassNotFoundException e) {
            log(Log.WARN, TAG, "MISSING " + name);
        } catch (Throwable t) {
            log(Log.ERROR, TAG, "Could not inspect " + name, t);
        }
    }
}
