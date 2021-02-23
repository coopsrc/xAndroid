package com.coopsrc.xandroid.dewdrops.processor;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import com.coopsrc.xandroid.dewdrops.config.BlurConfig;
import com.coopsrc.xandroid.dewdrops.renderscript.ScriptC_BoxBlur;
import com.coopsrc.xandroid.dewdrops.renderscript.ScriptC_StackBlur;
import com.coopsrc.xandroid.dewdrops.utils.MathUtils;
import com.coopsrc.xandroid.dewdrops.utils.Preconditions;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-10-12 11:04
 */
class RenderScriptBlurProcessor extends AbsBlurProcessor {
    private static final String TAG = "RenderScriptBlurProcess";

    private RenderScript renderScript;

    private ScriptIntrinsicBlur gaussianBlur;
    private ScriptC_StackBlur stackBlur;
    private ScriptC_BoxBlur boxBlur;

    RenderScriptBlurProcessor(ProcessorBuilder processorBuilder) {
        super(processorBuilder);
        radius = MathUtils.clamp(radius, 0, BlurConfig.RS_MAX_RADIUS);
        initRenderScript(processorBuilder.context);

    }

    @Override
    protected Bitmap doRealBlur(Bitmap bitmap, boolean concurrent) {
        Log.v(TAG, "doRealBlur: ");

        Preconditions.checkNotNull(bitmap, "scaledInBitmap == null");

        Allocation allocationIn = Allocation.createFromBitmap(renderScript, bitmap);
        Allocation allocationOut = Allocation.createFromBitmap(renderScript, Bitmap.createBitmap(bitmap));

        try {
            switch (mode) {
                case BlurConfig.MODE_BOX:
                    doBoxBlur(bitmap, allocationIn, allocationOut);
                    allocationIn.copyTo(bitmap);
                    break;
                case BlurConfig.MODE_GAUSSIAN:
                    doGaussianBlur(allocationIn, allocationOut);
                    allocationOut.copyTo(bitmap);
                    break;
                case BlurConfig.MODE_STACK:
                    doStackBlur(bitmap, allocationIn, allocationOut);
                    allocationIn.copyTo(bitmap);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            allocationIn.destroy();
            allocationOut.destroy();
        }

        return bitmap;
    }

    private void initRenderScript(Context context) {
        Preconditions.checkNotNull(context, "Please set context for renderscript scheme, forget to set context for builder?");

        try {
            renderScript = RenderScript.create(context.getApplicationContext());
            gaussianBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            stackBlur = new ScriptC_StackBlur(renderScript);
            boxBlur = new ScriptC_BoxBlur(renderScript);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doGaussianBlur(Allocation in, Allocation out) {
        Log.v(TAG, "doGaussianBlur: ");
        if (gaussianBlur == null) {
            throw new IllegalStateException("The blur script is unavailable");
        }
        // RenderScript won't work, if too large blur radius
        gaussianBlur.setRadius(radius);
        gaussianBlur.setInput(in);
        gaussianBlur.forEach(out);
    }

    private void doStackBlur(Bitmap input, Allocation in, Allocation out) {
        Log.v(TAG, "doStackBlur: ");
        if (stackBlur == null) {
            throw new IllegalStateException("The blur script is unavailable");
        }

        stackBlur.set_input(in);
        stackBlur.set_output(out);
        stackBlur.set_width(input.getWidth());
        stackBlur.set_height(input.getHeight());
        stackBlur.set_radius(radius);
        stackBlur.forEach_stackblur_v(in);

        stackBlur.set_input(out);
        stackBlur.set_output(in);
        stackBlur.forEach_stackblur_h(out);
    }

    private void doBoxBlur(Bitmap input, Allocation in, Allocation out) {
        Log.v(TAG, "doBoxBlur: ");
        if (boxBlur == null) {
            throw new IllegalStateException("The blur script is unavailable");
        }

        boxBlur.set_input(in);
        boxBlur.set_output(out);
        boxBlur.set_width(input.getWidth());
        boxBlur.set_height(input.getHeight());
        boxBlur.set_radius(radius);
        boxBlur.forEach_boxblur_h(in);

        boxBlur.set_input(out);
        boxBlur.set_output(in);
        boxBlur.forEach_boxblur_v(out);

    }
}
