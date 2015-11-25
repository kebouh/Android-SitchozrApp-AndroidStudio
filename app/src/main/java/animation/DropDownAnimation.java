package animation;

import android.animation.Animator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import sources.sitchozt.R;

/**
 * Created by kebouh on 25/11/15.
 */
public class DropDownAnimation {

    LinearLayout    controller;
    LinearLayout    controllerTotal;
    ImageView       arrow;
    Activity         context;
    int             content;
    int             duration;
    int             rootLayout;
    View            rootView;
    public DropDownAnimation(Activity context, int rootLayout, int content, int duration) {
        this.context = context;
        this.rootLayout = rootLayout;
        this.content = content;
        this.duration = duration;
    }

    public DropDownAnimation(View rootView, int rootLayout, int content, int duration) {
        this.rootView = rootView;
        this.rootLayout = rootLayout;
        this.content = content;
        this.duration = duration;
    }

    public void     initializeDropDown(LayoutInflater inflater) {

        if (rootView != null) {
            inflater.inflate(R.layout.dropdown_layout, (ViewGroup)rootView.findViewById(rootLayout));
            controller = (LinearLayout) rootView.findViewById(R.id.match_controller);
            controllerTotal = (LinearLayout) rootView.findViewById(R.id.match_controller_total);
            inflater.inflate(content, controller);
        }
        else {
            context.getLayoutInflater().inflate(R.layout.dropdown_layout, (ViewGroup)context.findViewById(rootLayout));
            controller = (LinearLayout) context.findViewById(R.id.match_controller);
            controllerTotal = (LinearLayout) context.findViewById(R.id.match_controller_total);
            context.getLayoutInflater().inflate(content, controller);
        }
        arrow = (ImageView) controllerTotal.findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controller.getVisibility() == View.INVISIBLE || controller.getVisibility() == View.GONE) {
                    ViewPropertyAnimator animator = controllerTotal.animate().y(0);
                    animator.setDuration(DropDownAnimation.this.duration);
                    animator.start();
                    controller.setVisibility(View.VISIBLE);
                } else {
                    hideController();
                }
            }
        });
        controllerTotal.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                controllerTotal.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                hideController();
            }
        });
    }

    public void     hideController() {
        final ViewPropertyAnimator animator = controllerTotal.animate().y(0 - /*arrow.getY()*/controller.getHeight());
        animator.setDuration(this.duration);
        animator.start();
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                controller.setVisibility(View.INVISIBLE);
                animator.setListener(null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

}
