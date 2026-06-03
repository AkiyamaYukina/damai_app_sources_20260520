package cn.damai.ultron.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.damai.common.app.TouchHelper;
import cn.damai.common.app.TouchLogHelper;
import cn.damai.commonbusiness.servicenotice.OnCompleteListener;
import cn.damai.commonbusiness.servicenotice.ProjectSupportServiceFragment;
import cn.damai.h5container.InsurancePurchaseActivity;
import cn.damai.pay.AliPayActivity;
import cn.damai.ticklet.bean.UserTicketTable;
import cn.damai.ultron.R$anim;
import cn.damai.ultron.R$color;
import cn.damai.ultron.R$id;
import cn.damai.ultron.R$layout;
import cn.damai.ultron.net.UltronPresenter;
import cn.damai.ultron.secondpage.selfaddress.view.DmSelfAddressBottomSheetFragment;
import cn.damai.ultron.utils.DmBuildRequestCallBackImp;
import cn.damai.ultron.utils.DmUltronChooseListenerImpl;
import cn.damai.ultron.utils.DmUltronRequestErrorUtils;
import cn.damai.ultron.view.OrderErrorPageView;
import cn.damai.ultron.view.bean.DmUltronPayResultBean;
import com.alibaba.pictures.bricks.base.PicturesBaseActivity;
import com.alibaba.pictures.bricks.util.Md5Util;
import com.alibaba.pictures.bricks.util.PioneerUIModeHelper;
import com.alibaba.pictures.bricks.util.SetUtil;
import com.alibaba.pictures.cornerstone.APPClient;
import com.alibaba.pictures.cornerstone.Cornerstone;
import com.alibaba.pictures.cornerstone.proxy.AppInfoProxy;
import com.alibaba.pictures.cornerstone.proxy.CloudConfigProxy;
import com.alibaba.pictures.cornerstone.proxy.NavigatorProxy;
import com.alibaba.pictures.mountain.MountainNative;
import com.alibaba.pictures.picturesbiz.NavUri;
import com.alibaba.pictures.picturesbiz.base.BizKey;
import com.alibaba.pictures.ut.DogCat;
import com.alibaba.pictures.ut.UTManager;
import com.alibaba.surgeon.bridge.ISurgeon;
import com.alibaba.surgeon.instrument.InstrumentAPI;
import com.alient.gaiax.container.util.ChannelUtil;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.android.ultron.common.model.IDMComponent;
import com.taobao.android.ultron.utils.DebugUtils;
import com.tencent.connect.common.Constants;
import com.youku.arch.v3.page.state.OnCreateStateViewListener;
import com.youku.arch.v3.page.state.PageStateManager;
import com.youku.arch.v3.page.state.State;
import com.youku.arch.v3.page.state.StateView;
import com.youku.middlewareservice.provider.info.AppInfoProviderProxy;
import com.youku.middlewareservice.provider.kvdata.SPProviderProxy;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import tb.dj3;
import tb.em0;
import tb.en3;
import tb.hi3;
import tb.hl3;
import tb.jn0;
import tb.kn0;
import tb.oh3;
import tb.oi0;
import tb.qm0;
import tb.qm0.c;
import tb.rm0;
import tb.s20;
import tb.sm0;
import tb.tm0;
import tb.tr2;
import tb.xl0;
import tb.yg3;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes17.dex */
public class DmOrderActivity extends PicturesBaseActivity {
    private static transient /* synthetic */ ISurgeon $surgeonFlag;
    public xl0 dmErrorViewHolder;
    private DmUltronPayResultBean dmPayResultBean;
    private View errorMask;
    private View errorMaskClick;
    Fragment fragment;
    private s20 mDMMessage;
    private qm0 mDmTicketDetailView;
    InputMethodManager mInputMethodManager;
    private UltronPresenter mPresenter;
    private LinearLayout mTicketPopView;
    private DMProgressDialogV2 progressDialog;
    private View statusBar;
    public String promotionDescNew = "";
    boolean is_seat = false;
    private boolean hasReportTouch = false;
    private final DmBuildRequestCallBackImp callBackImp = new b();
    private boolean backToDetail = false;
    private tr2.a header = null;
    DmUltronChooseListenerImpl<String> closeTicketDetailListener = new e();
    public int statusBarHeight = 0;

    /* JADX INFO: compiled from: Taobao */
    /* JADX INFO: loaded from: classes23.dex */
    public enum FragmentType {
        SELFADDRESS,
        PROMOTION,
        NOTICE
    }

    /* JADX INFO: compiled from: Taobao */
    public class a implements TouchHelper.OnAnomalyListener {
        private static transient /* synthetic */ ISurgeon $surgeonFlag;

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        public a() {
        }

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        @Override // cn.damai.common.app.TouchHelper.OnAnomalyListener
        public final void onTouchEvent(int i, @NonNull String str) {
            ISurgeon iSurgeon = $surgeonFlag;
            if (InstrumentAPI.support(iSurgeon, "1")) {
                iSurgeon.surgeon$dispatch("1", new Object[]{this, Integer.valueOf(i), str});
                return;
            }
            DmOrderActivity dmOrderActivity = DmOrderActivity.this;
            if (dmOrderActivity.hasReportTouch) {
                return;
            }
            HashMap map = new HashMap();
            map.put("score", String.valueOf(i));
            map.put("reason", str);
            DogCat.INSTANCE.custom().eventName("touch_log_report").params(map).commit();
            dmOrderActivity.hasReportTouch = true;
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public class b implements DmBuildRequestCallBackImp {
        private static transient /* synthetic */ ISurgeon $surgeonFlag;

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        public b() {
        }

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        @Override // cn.damai.ultron.utils.DmBuildRequestCallBackImp
        public final void onError(String str, String str2, int i, String str3, String str4) {
            ISurgeon iSurgeon = $surgeonFlag;
            if (InstrumentAPI.support(iSurgeon, "1")) {
                iSurgeon.surgeon$dispatch("1", new Object[]{this, str, str2, Integer.valueOf(i), str3, str4});
                return;
            }
            if (i == 420) {
                str2 = "抢票人数太多啦，继续尝试别放弃";
            }
            DmUltronRequestErrorUtils.e().f(DmUltronRequestErrorUtils.BizType.BUILD).g(DmUltronRequestErrorUtils.DefaultError.ERROR_LAYOUT).i(DmUltronRequestErrorUtils.NetError.NO_NETWORK).c(DmOrderActivity.this, str, str2, str3, str4);
        }

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        /* JADX WARN: Type inference failed for: r0v1, types: [android.app.Activity, cn.damai.ultron.view.activity.DmOrderActivity] */
        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        @Override // cn.damai.ultron.utils.DmBuildRequestCallBackImp
        public final void onSuccess() {
            ISurgeon iSurgeon = $surgeonFlag;
            if (InstrumentAPI.support(iSurgeon, "2")) {
                iSurgeon.surgeon$dispatch("2", new Object[]{this});
                return;
            }
            ?? r0 = DmOrderActivity.this;
            xl0 xl0Var = r0.dmErrorViewHolder;
            if (xl0Var != null) {
                xl0Var.c();
            }
            r0.hideErrorView(r0);
            r0.updateTicketDetailData();
        }
    }

    /* JADX INFO: compiled from: Taobao */
    /* JADX INFO: loaded from: classes23.dex */
    public class c implements View.OnClickListener {
        private static transient /* synthetic */ ISurgeon $surgeonFlag;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ long f5215a;
        public final /* synthetic */ int b;

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        public c(long j, int i) {
            this.f5215a = j;
            this.b = i;
        }

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ISurgeon iSurgeon = $surgeonFlag;
            if (InstrumentAPI.support(iSurgeon, "1")) {
                iSurgeon.surgeon$dispatch("1", new Object[]{this, view});
            } else if (System.currentTimeMillis() - this.f5215a > this.b) {
                DmOrderActivity.this.errorMaskClick.setClickable(false);
                DmOrderActivity.this.errorMaskClick.setOnClickListener(null);
                DmOrderActivity.this.errorMask.setVisibility(8);
            }
        }
    }

    /* JADX INFO: compiled from: Taobao */
    /* JADX INFO: loaded from: classes23.dex */
    public class d implements View.OnClickListener {
        private static transient /* synthetic */ ISurgeon $surgeonFlag;

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        public d() {
        }

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ISurgeon iSurgeon = $surgeonFlag;
            if (InstrumentAPI.support(iSurgeon, "1")) {
                iSurgeon.surgeon$dispatch("1", new Object[]{this, view});
            } else {
                DmOrderActivity.this.finish();
            }
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public class e implements DmUltronChooseListenerImpl<String> {
        private static transient /* synthetic */ ISurgeon $surgeonFlag;

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        public e() {
        }

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // cn.damai.ultron.utils.DmUltronChooseListenerImpl
        public final void chooseItemListener(String str) {
            String str2 = str;
            ISurgeon iSurgeon = $surgeonFlag;
            if (InstrumentAPI.support(iSurgeon, "1")) {
                iSurgeon.surgeon$dispatch("1", new Object[]{this, str2});
            } else {
                DmOrderActivity dmOrderActivity = DmOrderActivity.this;
                dmOrderActivity.mPresenter.getTradeEventHandler().dispatchEvent(dmOrderActivity.mPresenter.getTradeEventHandler().buildTradeEvent().setEventType(tm0.closePopUpEvent));
            }
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public class f implements ProjectSupportServiceFragment.AfterCreateViewHook {
        private static transient /* synthetic */ ISurgeon $surgeonFlag;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ ProjectSupportServiceFragment f5218a;
        public final /* synthetic */ PicturesBaseActivity b;

        /* JADX INFO: compiled from: Taobao */
        public class a implements OnCompleteListener {
            private static transient /* synthetic */ ISurgeon $surgeonFlag;

            /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
            public a() {
            }

            /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
            @Override // cn.damai.commonbusiness.servicenotice.OnCompleteListener
            public final void onComplete(int i) {
                ISurgeon iSurgeon = $surgeonFlag;
                if (InstrumentAPI.support(iSurgeon, "1")) {
                    iSurgeon.surgeon$dispatch("1", new Object[]{this, Integer.valueOf(i)});
                    return;
                }
                PicturesBaseActivity picturesBaseActivity = f.this.b;
                if (picturesBaseActivity == null || !(picturesBaseActivity instanceof DmOrderActivity)) {
                    return;
                }
                ((DmOrderActivity) picturesBaseActivity).hideProFragment();
            }
        }

        /* JADX INFO: compiled from: Taobao */
        /* JADX INFO: loaded from: classes23.dex */
        public class b implements View.OnClickListener {
            private static transient /* synthetic */ ISurgeon $surgeonFlag;

            /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
            public b() {
            }

            /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ISurgeon iSurgeon = $surgeonFlag;
                if (InstrumentAPI.support(iSurgeon, "1")) {
                    iSurgeon.surgeon$dispatch("1", new Object[]{this, view});
                    return;
                }
                PicturesBaseActivity picturesBaseActivity = f.this.b;
                if (picturesBaseActivity == null || !(picturesBaseActivity instanceof DmOrderActivity)) {
                    return;
                }
                ((DmOrderActivity) picturesBaseActivity).hideProFragment();
            }
        }

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        public f(ProjectSupportServiceFragment projectSupportServiceFragment, PicturesBaseActivity picturesBaseActivity) {
            this.f5218a = projectSupportServiceFragment;
            this.b = picturesBaseActivity;
        }

        /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
        @Override // cn.damai.commonbusiness.servicenotice.ProjectSupportServiceFragment.AfterCreateViewHook
        public final void afterCreateView() {
            ISurgeon iSurgeon = $surgeonFlag;
            if (InstrumentAPI.support(iSurgeon, "1")) {
                iSurgeon.surgeon$dispatch("1", new Object[]{this});
                return;
            }
            a aVar = new a();
            ProjectSupportServiceFragment projectSupportServiceFragment = this.f5218a;
            projectSupportServiceFragment.setOnCompleteListener(aVar);
            View view = projectSupportServiceFragment.outView;
            view.setBackgroundColor(Color.parseColor("#7E000000"));
            view.setOnClickListener(new b());
            projectSupportServiceFragment.layoutBottom.startAnimation(AnimationUtils.loadAnimation(this.b, R$anim.activity_item_animshow));
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void InitUltronPresenter(Bundle bundle) {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "6")) {
            iSurgeon.surgeon$dispatch("6", new Object[]{this, bundle});
            return;
        }
        closeDebugMarker();
        RecyclerView recyclerView = (RecyclerView) findViewById(R$id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        LinearLayout linearLayout = (LinearLayout) findViewById(R$id.bottom_layout);
        UltronPresenter ultronPresenter = new UltronPresenter(this, this.callBackImp);
        this.mPresenter = ultronPresenter;
        ultronPresenter.onCreate(bundle);
        this.mPresenter.getViewManager().setErrorPageView(this.dmErrorViewHolder);
        this.mPresenter.initView(null, recyclerView, linearLayout);
        this.mPresenter.setProjectInfo(this.header);
        this.mPresenter.buildPage();
        s20 s20Var = new s20();
        this.mDMMessage = s20Var;
        this.mPresenter.listenerNotify(s20Var);
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void InputMethodHide() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "32")) {
            iSurgeon.surgeon$dispatch("32", new Object[]{this});
            return;
        }
        if (this.mInputMethodManager == null) {
            this.mInputMethodManager = (InputMethodManager) getSystemService("input_method");
        }
        this.mInputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    private static void closeDebugMarker() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "7")) {
            iSurgeon.surgeon$dispatch("7", new Object[0]);
            return;
        }
        if (!AppInfoProviderProxy.isDebuggable() || Cornerstone.getLocalKV().getBoolean("dx_water_mark_enabled", false)) {
            return;
        }
        try {
            Field declaredField = DebugUtils.class.getDeclaredField("sDebuggable");
            declaredField.setAccessible(true);
            declaredField.set(null, Boolean.FALSE);
        } catch (Exception e2) {
            Log.e("UltronPresenter", "Failed to preset DebugUtils.sDebuggable via reflection", e2);
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    private void initBackEvent() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "18")) {
            iSurgeon.surgeon$dispatch("18", new Object[]{this});
        } else {
            findViewById(R$id.tv_goback).setOnClickListener(new d());
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    private void initErrorMask() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "10")) {
            iSurgeon.surgeon$dispatch("10", new Object[]{this});
        } else {
            this.errorMask = findViewById(R$id.a_fast_click_blocker_container);
            this.errorMaskClick = findViewById(R$id.a_fast_click_blocker);
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    private void initErrorView() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "21")) {
            iSurgeon.surgeon$dispatch("21", new Object[]{this});
            return;
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R$id.error_layout);
        xl0 xl0Var = new xl0();
        this.dmErrorViewHolder = xl0Var;
        xl0Var.e(linearLayout, this, this.header);
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void initParams() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "3")) {
            iSurgeon.surgeon$dispatch("3", new Object[]{this});
            return;
        }
        try {
            Intent intent = getIntent();
            long longExtra = 0;
            if (intent != null) {
                if (intent.getBooleanExtra("isSpliceOrder", false)) {
                    ((TextView) findViewById(R$id.order_activity_title)).setText("确认拼单");
                } else {
                    ((TextView) findViewById(R$id.order_activity_title)).setText("确认购买");
                }
                long longExtra2 = intent.getLongExtra(cn.damai.commonbusiness.nav.a.KEY_DM_ITEM_ID, 0L);
                if (longExtra2 == 0 && !TextUtils.isEmpty(intent.getStringExtra("itemId"))) {
                    try {
                        longExtra2 = Long.parseLong(intent.getStringExtra("itemId"));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                longExtra = longExtra2 == 0 ? intent.getLongExtra("itemId", 0L) : longExtra2;
                if (intent.hasExtra("is_seat")) {
                    this.is_seat = intent.getBooleanExtra("is_seat", false);
                } else {
                    String stringExtra = intent.getStringExtra("chooseSeat");
                    if (TextUtils.isEmpty(stringExtra) || !"1".equals(stringExtra)) {
                        this.is_seat = false;
                    } else {
                        this.is_seat = true;
                    }
                }
                tr2.a aVarA = tr2.a(longExtra);
                this.header = aVarA;
                if (aVarA != null) {
                    aVarA.j = intent.getBooleanExtra("quickSeatSelection", false) ? "1" : "0";
                    if (this.header.j.equals("1") && !SetUtil.isEmpty(this.header.e) && ((ArrayList) this.header.e).get(0) != null) {
                        tr2.b bVar = (tr2.b) ((ArrayList) this.header.e).get(0);
                        String str = bVar.f15723a;
                        String str2 = bVar.d;
                        jn0.q().I("header_cache", longExtra + "", null, null, str2, str);
                    }
                }
            }
            em0.g(longExtra, this);
            em0.f(this, this.is_seat);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void initStateBar() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, UserTicketTable.LIVE_TICKET)) {
            iSurgeon.surgeon$dispatch(UserTicketTable.LIVE_TICKET, new Object[]{this});
            return;
        }
        View viewFindViewById = findViewById(R$id.status_bar);
        this.statusBar = viewFindViewById;
        if (Build.VERSION.SDK_INT < 23) {
            hl3.f(this, false, R$color.black);
            View view = this.statusBar;
            if (view != null) {
                view.setVisibility(8);
                return;
            }
            return;
        }
        if (viewFindViewById != null) {
            this.statusBarHeight = hl3.a(this);
            this.statusBar.getLayoutParams().height = this.statusBarHeight;
            this.statusBar.setVisibility(0);
        }
        hl3.f(this, true, R$color.black);
        hl3.d(true, this);
        hl3.e(this);
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    private void initSuggestData() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "34")) {
            iSurgeon.surgeon$dispatch("34", new Object[]{this});
            return;
        }
        if (ChannelUtil.INSTANCE.isTppApp()) {
            return;
        }
        String md5 = Md5Util.getMD5(AppInfoProviderProxy.getPackageName().substring(1));
        if (!CloudConfigProxy.INSTANCE.isExpected("dm_suggest", "true", true)) {
            SPProviderProxy.getSharedPreferences(md5).edit().putString(md5, "").apply();
            return;
        }
        MountainNative mountainNative = new MountainNative();
        try {
            String string = getResources().getString(getResources().getIdentifier("dm_aultron_key", "string", AppInfoProviderProxy.getPackageName()));
            String sToken = mountainNative.getSToken();
            if (TextUtils.isEmpty(string)) {
                SPProviderProxy.getSharedPreferences(md5).edit().putString(md5, "").apply();
                return;
            }
            if (!TextUtils.isEmpty(sToken)) {
                string = string + sToken;
            }
            SPProviderProxy.getSharedPreferences(md5).edit().putString(md5, string).apply();
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            SPProviderProxy.getSharedPreferences(md5).edit().putString(md5, "").apply();
        } catch (UnsatisfiedLinkError e3) {
            e = e3;
            e.printStackTrace();
            SPProviderProxy.getSharedPreferences(md5).edit().putString(md5, "").apply();
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private void initTicketDetailView() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "23")) {
            iSurgeon.surgeon$dispatch("23", new Object[]{this});
            return;
        }
        this.mTicketPopView = (LinearLayout) findViewById(R$id.ll_popup);
        qm0 qm0Var = new qm0();
        qm0Var.j = new HashMap<>();
        qm0Var.k = qm0Var.new c();
        this.mDmTicketDetailView = qm0Var;
        qm0Var.c(this, this.mTicketPopView);
        this.mDmTicketDetailView.e(this.closeTicketDetailListener);
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public void callActivityResult(int i, int i2, Intent intent) {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "12")) {
            iSurgeon.surgeon$dispatch("12", new Object[]{this, Integer.valueOf(i), Integer.valueOf(i2), intent});
            return;
        }
        try {
            onActivityResult(i, i2, intent);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity
    public boolean enableUTReport() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "4")) {
            return ((Boolean) iSurgeon.surgeon$dispatch("4", new Object[]{this})).booleanValue();
        }
        return true;
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public int getCurrentIndex(String str) {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "36")) {
            return ((Integer) iSurgeon.surgeon$dispatch("36", new Object[]{this, str})).intValue();
        }
        try {
            return Integer.valueOf(str).intValue();
        } catch (Exception e2) {
            e2.printStackTrace();
            return 0;
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public int getLayoutId() {
        ISurgeon iSurgeon = $surgeonFlag;
        return InstrumentAPI.support(iSurgeon, "1") ? ((Integer) iSurgeon.surgeon$dispatch("1", new Object[]{this})).intValue() : R$layout.activity_ultron;
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public UltronPresenter getPresenter() {
        ISurgeon iSurgeon = $surgeonFlag;
        return InstrumentAPI.support(iSurgeon, "30") ? (UltronPresenter) iSurgeon.surgeon$dispatch("30", new Object[]{this}) : this.mPresenter;
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity
    @Nullable
    public String getUtPageName() {
        ISurgeon iSurgeon = $surgeonFlag;
        return InstrumentAPI.support(iSurgeon, "5") ? (String) iSurgeon.surgeon$dispatch("5", new Object[]{this}) : "confirm";
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void hideCustomLoadingDialog() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "39")) {
            iSurgeon.surgeon$dispatch("39", new Object[]{this});
            return;
        }
        if (isFinishing()) {
            return;
        }
        try {
            DMProgressDialogV2 dMProgressDialogV2 = this.progressDialog;
            if (dMProgressDialogV2 != null) {
                dMProgressDialogV2.dismiss();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity, com.alient.onearch.adapter.state.StateViewManager.IStateFeature
    public void hideErrorView(@Nullable Activity activity) {
        StateView stateView;
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "11")) {
            iSurgeon.surgeon$dispatch("11", new Object[]{this, activity});
            return;
        }
        int i = CloudConfigProxy.INSTANCE.getInt("ultron_error_view_mask_time", 500);
        if (i <= 0) {
            this.errorMask.setVisibility(8);
            super.hideErrorView(activity);
            return;
        }
        this.errorMask.postDelayed(new Runnable() { // from class: cn.damai.ultron.view.activity.DmOrderActivity.3
            private static transient /* synthetic */ ISurgeon $surgeonFlag;

            /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
            @Override // java.lang.Runnable
            public void run() {
                ISurgeon iSurgeon2 = $surgeonFlag;
                if (InstrumentAPI.support(iSurgeon2, "1")) {
                    iSurgeon2.surgeon$dispatch("1", new Object[]{this});
                } else if (DmOrderActivity.this.errorMask != null) {
                    ((View) new WeakReference(DmOrderActivity.this.errorMask).get()).setVisibility(8);
                }
            }
        }, i);
        long jCurrentTimeMillis = System.currentTimeMillis();
        State currentState = this.pageStateManager.getCurrentState();
        State state = State.FAILED;
        if (currentState == state && (stateView = this.pageStateManager.stateView) != null && (stateView.getStateView(state) instanceof OrderErrorPageView)) {
            this.errorMask.setVisibility(0);
            OrderErrorPageView orderErrorPageView = (OrderErrorPageView) this.pageStateManager.stateView.getStateView(state);
            if (orderErrorPageView != null && orderErrorPageView.isRefreshBtn()) {
                int[] btnLocation = orderErrorPageView.getBtnLocation();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.errorMaskClick.getLayoutParams();
                int i2 = btnLocation[1];
                if (i2 > 0) {
                    layoutParams.topMargin = i2 - (oi0.a(activity, 90.0f) / 2);
                } else {
                    layoutParams.topMargin = (int) (oi0.d(activity) * 0.6000000238418579d);
                }
                this.errorMaskClick.setLayoutParams(layoutParams);
                this.errorMaskClick.setOnClickListener(new c(jCurrentTimeMillis, i));
            }
        }
        super.hideErrorView(activity);
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity, com.alient.onearch.adapter.state.StateViewManager.IStateFeature
    public void hideLoadingDialog(@Nullable Activity activity) {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, PioneerUIModeHelper.CMS_ITEM_TITLE_HEIGHT_PIONEER_STR)) {
            iSurgeon.surgeon$dispatch(PioneerUIModeHelper.CMS_ITEM_TITLE_HEIGHT_PIONEER_STR, new Object[]{this, activity});
        } else {
            super.hideLoadingDialog(activity);
            hideCustomLoadingDialog();
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public void hideProFragment() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "17")) {
            iSurgeon.surgeon$dispatch("17", new Object[]{this});
            return;
        }
        if (this.fragment == null) {
            findViewById(R$id.ll_promotion).setVisibility(8);
            return;
        }
        FragmentTransaction fragmentTransactionBeginTransaction = getSupportFragmentManager().beginTransaction();
        findViewById(R$id.ll_promotion).setVisibility(8);
        fragmentTransactionBeginTransaction.remove(this.fragment);
        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public ProjectSupportServiceFragment initFragment(PicturesBaseActivity picturesBaseActivity, Bundle bundle) {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "35")) {
            return (ProjectSupportServiceFragment) iSurgeon.surgeon$dispatch("35", new Object[]{this, picturesBaseActivity, bundle});
        }
        if (bundle == null) {
            return null;
        }
        int currentIndex = getCurrentIndex(bundle.getString("index", "0"));
        ArrayList parcelableArrayList = bundle.getParcelableArrayList(hi3.notice);
        ArrayList parcelableArrayList2 = bundle.getParcelableArrayList("service");
        ArrayList parcelableArrayList3 = bundle.getParcelableArrayList("customut");
        if (en3.f(parcelableArrayList2) + en3.f(parcelableArrayList) == 0) {
            return null;
        }
        ProjectSupportServiceFragment projectSupportServiceFragmentGenInstance = ProjectSupportServiceFragment.genInstance(parcelableArrayList2, parcelableArrayList, parcelableArrayList3, currentIndex);
        projectSupportServiceFragmentGenInstance.setPioneerTheme(AppInfoProxy.INSTANCE.getAppConfigProvider().getIsPioneerOpen());
        projectSupportServiceFragmentGenInstance.setAfterCreateViewHook(new f(projectSupportServiceFragmentGenInstance, picturesBaseActivity));
        return projectSupportServiceFragmentGenInstance;
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity
    public void initPageStateManager() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "22")) {
            iSurgeon.surgeon$dispatch("22", new Object[]{this});
            return;
        }
        super.initPageStateManager();
        OrderErrorPageView orderErrorPageView = new OrderErrorPageView(this);
        PageStateManager pageStateManager = this.pageStateManager;
        State state = State.FAILED;
        pageStateManager.setStateProperty(state, (OnCreateStateViewListener) null);
        this.pageStateManager.setStateProperty(state, orderErrorPageView);
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity
    public boolean isHideReportBtn() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "33")) {
            return ((Boolean) iSurgeon.surgeon$dispatch("33", new Object[]{this})).booleanValue();
        }
        return true;
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void onActivityResult(int i, int i2, Intent intent) {
        DmUltronPayResultBean dmUltronPayResultBean;
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "13")) {
            iSurgeon.surgeon$dispatch("13", new Object[]{this, Integer.valueOf(i), Integer.valueOf(i2), intent});
            return;
        }
        super/*androidx.fragment.app.FragmentActivity*/.onActivityResult(i, i2, intent);
        if (i == 36 && i2 == -1) {
            IDMComponent iDMComponentA = rm0.a(this.mPresenter);
            if (iDMComponentA != null) {
                this.mPresenter.getDataManager().respondToLinkage(iDMComponentA);
                return;
            }
            return;
        }
        if (i == 41) {
            if (i2 == -1) {
                this.mPresenter.getTradeEventHandler().dispatchEvent(this.mPresenter.getTradeEventHandler().buildTradeEvent().setEventType(tm0.switchDeliveryWayEvent).setExtraData("data", intent));
                return;
            }
            return;
        }
        if (i == 48) {
            if (i2 == -1) {
                this.mPresenter.getTradeEventHandler().dispatchEvent(this.mPresenter.getTradeEventHandler().buildTradeEvent().setEventType(tm0.switchDataTypeEvent).setExtraData("pageType", tm0.PAGE_PHONE_CODE).setExtraData("data", intent));
                return;
            }
            return;
        }
        if (i == 37) {
            if (intent != null) {
                this.mPresenter.getTradeEventHandler().dispatchEvent(this.mPresenter.getTradeEventHandler().buildTradeEvent().setEventType(tm0.switchDataTypeEvent).setExtraData("pageType", tm0.PAGE_ADDRESS_LIST).setExtraData("data", intent));
                return;
            }
            return;
        }
        if (i == 49) {
            onSelectPromoBack(intent);
            return;
        }
        if (i == 39) {
            if (i2 != -1 || intent == null) {
                return;
            }
            this.mPresenter.getTradeEventHandler().dispatchEvent(this.mPresenter.getTradeEventHandler().buildTradeEvent().setEventType(tm0.switchDataTypeEvent).setExtraData("pageType", tm0.PAGE_READ_PHONE).setExtraData("data", intent));
            return;
        }
        if (i == 38) {
            finish();
            return;
        }
        if (i == 50) {
            DmUltronPayResultBean dmUltronPayResultBean2 = this.dmPayResultBean;
            if (dmUltronPayResultBean2 != null) {
                kn0.f(this, dmUltronPayResultBean2);
                return;
            } else {
                Log.e("payresult", "pay result is null after anim");
                return;
            }
        }
        if (i == 51) {
            if (i2 == -1 && (dmUltronPayResultBean = this.dmPayResultBean) != null) {
                kn0.f(this, dmUltronPayResultBean);
                return;
            }
            if (this.dmPayResultBean != null) {
                Bundle bundle = new Bundle();
                bundle.putString("orderId", this.dmPayResultBean.bizOrderId);
                bundle.putBoolean(InsurancePurchaseActivity.ENTRY_PAY_RESULT, true);
                bundle.putBoolean(AliPayActivity.FROM_HN_CRETE_ORDER_PAGE, true);
                AppInfoProxy appInfoProxy = AppInfoProxy.INSTANCE;
                if (appInfoProxy.getAppClientName().equals(APPClient.TPP.getClientName())) {
                    NavigatorProxy.INSTANCE.handleUrl(this, "tbmovie://taobao.com/home");
                } else {
                    appInfoProxy.getValueByType(BizKey.BizKey_NAV_CLEARTOP, null);
                }
                NavigatorProxy.INSTANCE.handleUri(this, NavUri.page("my_hn_orderdetails").build(), bundle);
                finish();
                Log.e("payresult", "pay result is null after anim");
            }
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public void onBackPressed() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "19")) {
            iSurgeon.surgeon$dispatch("19", new Object[]{this});
            return;
        }
        if (this.backToDetail) {
            DmUltronRequestErrorUtils.e().k(this);
            return;
        }
        Fragment fragment = this.fragment;
        if (fragment != null && fragment.isVisible() && findViewById(R$id.ll_promotion).getVisibility() == 0) {
            hideProFragment();
        } else {
            super/*androidx.activity.ComponentActivity*/.onBackPressed();
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "40")) {
            iSurgeon.surgeon$dispatch("40", new Object[]{this, configuration});
            return;
        }
        super.onConfigurationChanged(configuration);
        Log.e("onxConfigurationChanged", "order onConfigurationChanged start  ");
        if (!CloudConfigProxy.INSTANCE.isExpected("order_onConfigurationChanged_switch", "open", true)) {
            Log.e("onxConfigurationChanged", "order onConfigurationChanged closed by orange ");
            return;
        }
        Log.e("onxConfigurationChanged", "order onConfigurationChanged excute ");
        UltronPresenter ultronPresenter = this.mPresenter;
        if (ultronPresenter != null) {
            if (!sm0.f15549a) {
                ultronPresenter.buildPage();
            } else {
                sm0.f15549a = false;
                onBackPressed();
            }
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity
    public void onCreate(@Nullable Bundle bundle) {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "2")) {
            iSurgeon.surgeon$dispatch("2", new Object[]{this, bundle});
            return;
        }
        UTManager.INSTANCE.registerPageSpm("confirm", "confirm");
        supportRequestWindowFeature(1);
        super.onCreate(bundle);
        jn0.q().f14184a = 0L;
        this.backToDetail = false;
        setContentView(getLayoutId());
        if (bundle != null) {
            DMOrderLogUtil.enableLog = true;
        }
        dj3.INSTANCE.a(this);
        initStateBar();
        initErrorMask();
        PrintStream printStream = System.out;
        printStream.println("aultron on Create");
        initParams();
        initSuggestData();
        startExpoTrack(this);
        initErrorView();
        InitUltronPresenter(bundle);
        initBackEvent();
        initTicketDetailView();
        printStream.println("aultron on Create finish");
        AppInfoProxy appInfoProxy = AppInfoProxy.INSTANCE;
        if (appInfoProxy.getAppConfigProvider() != null && appInfoProxy.getAppConfigProvider().getIsPioneerOpen()) {
            findViewById(R$id.top_layout).setBackgroundColor(Color.parseColor("#FF5000"));
        }
        if (Cornerstone.getCloudConfig().isExpected("enableTouchLog", DAttrConstant.VIEW_EVENT_FLAG, true)) {
            TouchLogHelper.INSTANCE.e(this, new a());
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity
    public void onDestroy() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "29")) {
            iSurgeon.surgeon$dispatch("29", new Object[]{this});
            return;
        }
        UltronPresenter ultronPresenter = this.mPresenter;
        if (ultronPresenter != null) {
            ultronPresenter.onDestroy();
        }
        s20 s20Var = this.mDMMessage;
        if (s20Var != null) {
            s20Var.a();
        }
        InputMethodHide();
        yg3.g().y();
        jn0.q().f14184a = 0L;
        if (!this.is_seat) {
            oh3.a();
        }
        TouchLogHelper.INSTANCE.f();
        sm0.f15549a = false;
        super.onDestroy();
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity
    public void onPause() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "27")) {
            iSurgeon.surgeon$dispatch("27", new Object[]{this});
            return;
        }
        super.onPause();
        UltronPresenter ultronPresenter = this.mPresenter;
        if (ultronPresenter != null) {
            ultronPresenter.onPause();
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity, com.alient.onearch.adapter.state.StateViewManager.IStateViewListener
    public boolean onRefreshClick() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "8")) {
            return ((Boolean) iSurgeon.surgeon$dispatch("8", new Object[]{this})).booleanValue();
        }
        this.mPresenter.buildPage();
        return true;
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity, com.alient.onearch.adapter.state.StateViewManager.IStateViewListener
    public boolean onReportClick() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "9")) {
            return ((Boolean) iSurgeon.surgeon$dispatch("9", new Object[]{this})).booleanValue();
        }
        return false;
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.alibaba.pictures.bricks.base.PicturesBaseActivity
    public void onResume() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "26")) {
            iSurgeon.surgeon$dispatch("26", new Object[]{this});
            return;
        }
        super.onResume();
        UltronPresenter ultronPresenter = this.mPresenter;
        if (ultronPresenter != null) {
            ultronPresenter.onResume();
        }
        HashMap map = new HashMap();
        map.put("item_id", String.valueOf(em0.b(this)));
        map.put("is_seat", em0.a(this) ? "1" : "0");
        DogCat.INSTANCE.updatePageProperties(this, map);
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public void onSelectPromoBack(Intent intent) {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "15")) {
            iSurgeon.surgeon$dispatch("15", new Object[]{this, intent});
        } else if (intent != null) {
            this.mPresenter.getTradeEventHandler().dispatchEvent(this.mPresenter.getTradeEventHandler().buildTradeEvent().setEventType(tm0.switchDataTypeEvent).setExtraData("pageType", tm0.PAGE_PROMOTION_LIST).setExtraData("data", intent));
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public void onStop() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, Constants.VIA_ACT_TYPE_TWENTY_EIGHT)) {
            iSurgeon.surgeon$dispatch(Constants.VIA_ACT_TYPE_TWENTY_EIGHT, new Object[]{this});
        } else {
            super.onStop();
            InputMethodHide();
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public void setBackToDetailMode() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "20")) {
            iSurgeon.surgeon$dispatch("20", new Object[]{this});
        } else {
            this.backToDetail = true;
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public void setPayResult(DmUltronPayResultBean dmUltronPayResultBean) {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "14")) {
            iSurgeon.surgeon$dispatch("14", new Object[]{this, dmUltronPayResultBean});
        } else {
            this.dmPayResultBean = dmUltronPayResultBean;
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void showCustomLoadingDialog(@Nullable String str, boolean z) {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "37")) {
            iSurgeon.surgeon$dispatch("37", new Object[]{this, str, Boolean.valueOf(z)});
            return;
        }
        if (isFinishing()) {
            return;
        }
        try {
            if (this.progressDialog == null) {
                this.progressDialog = new DMProgressDialogV2(this).a();
            }
            this.progressDialog.setCancelable(z);
            if (this.progressDialog.isShowing() || isFinishing()) {
                return;
            }
            this.progressDialog.show();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public void showProFragment(Bundle bundle, FragmentType fragmentType) {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "16")) {
            iSurgeon.surgeon$dispatch("16", new Object[]{this, bundle, fragmentType});
            return;
        }
        if (fragmentType == FragmentType.SELFADDRESS) {
            DmSelfAddressBottomSheetFragment dmSelfAddressBottomSheetFragment = new DmSelfAddressBottomSheetFragment();
            dmSelfAddressBottomSheetFragment.setArguments(bundle);
            dmSelfAddressBottomSheetFragment.show(getSupportFragmentManager(), "DmSelfAddressBottomSheetFragment");
            return;
        }
        if (fragmentType == FragmentType.PROMOTION) {
            DmUltronPromotionFragment dmUltronPromotionFragment = new DmUltronPromotionFragment();
            dmUltronPromotionFragment.setArguments(bundle);
            dmUltronPromotionFragment.show(getSupportFragmentManager(), "DmUltronPromotionFragment");
        } else if (fragmentType == FragmentType.NOTICE) {
            ProjectSupportServiceFragment projectSupportServiceFragmentInitFragment = initFragment(this, bundle);
            this.fragment = projectSupportServiceFragmentInitFragment;
            if (projectSupportServiceFragmentInitFragment == null) {
                return;
            }
            FragmentTransaction fragmentTransactionBeginTransaction = getSupportFragmentManager().beginTransaction();
            int i = R$id.ll_promotion;
            findViewById(i).setVisibility(0);
            this.fragment.setArguments(bundle);
            fragmentTransactionBeginTransaction.replace(i, this.fragment);
            fragmentTransactionBeginTransaction.commitAllowingStateLoss();
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public void updateTicketDetailData() {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "24")) {
            iSurgeon.surgeon$dispatch("24", new Object[]{this});
        } else {
            this.mDmTicketDetailView.g(rm0.j(this.mPresenter));
        }
    }

    /* JADX DEBUG: Don't trust debug lines info. Lines numbers was adjusted: min line is 1 */
    public void updateTicketDetailVis(boolean z) {
        ISurgeon iSurgeon = $surgeonFlag;
        if (InstrumentAPI.support(iSurgeon, "25")) {
            iSurgeon.surgeon$dispatch("25", new Object[]{this, Boolean.valueOf(z)});
        } else if (z) {
            this.mDmTicketDetailView.b();
        } else {
            this.mDmTicketDetailView.f();
        }
    }
}
