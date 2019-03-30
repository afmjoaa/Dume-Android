package io.dume.dume.common.chatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.Pulse;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.jaeger.library.StatusBarUtil;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import carbon.widget.ImageView;
import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.common.chatActivity.Holders.IncomingVoiceMessageViewHolder;
import io.dume.dume.common.chatActivity.Holders.OutcomingVoiceMessageViewHolder;
import io.dume.dume.common.chatActivity.Used_Classes.DemoMessagesActivity;
import io.dume.dume.common.chatActivity.Used_Classes.Message;
import io.dume.dume.common.chatActivity.Used_Classes.MessagesFixtures;
import io.dume.dume.common.chatActivity.Used_Classes.User;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.pojo.Letter;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.configureAppbarWithoutColloapsing;

public class ChatActivity extends DemoMessagesActivity implements ChatActivityContact.View,
        MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageHolders.ContentChecker<Message>,
        DialogInterface.OnClickListener {

    private ChatActivityContact.Presenter mPresenter;
    public static boolean isActivityLive = false;
    private static final byte CONTENT_TYPE_VOICE = 1;
    private LinearLayout attachmentHolder;
    private View inbetweenDivider;
    private LinearLayout transitionHostLayout;
    private LinearLayout attachmentDocument;
    private ImageView attachemtDocumentImage;
    private FrameLayout viewMusk;
    private ChatActivityModel mModel;
    public static int SENDER = 0, RECIVER = 1;
    public static int TYPE;
    private MessageInput input;
    private TextView typeingTV;
    private TextView title;
    private ImageView dp;
    public boolean isDataExists = false;
    private static FirebaseMessaging notificationInstance;
    private String token = null;
    private String name = null;
    private Google google;
    private RelativeLayout loadingProgressBar;
    private ProgressBar progressBar;
    private RelativeLayout hideableTypingRelative;
    private ProgressBar progressBarTypingTwo;
    private LinearLayout noDataBlock;
    private String avatar= null;
    private ImageView typingAvatar;

    @Override
    protected void onPause() {
        super.onPause();
        isActivityLive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityLive = true;
    }

    public static void open(Context context) {
        context.startActivity(new Intent(context, ChatActivity.class));
        notificationInstance = FirebaseMessaging.getInstance();
        RemoteMessage.Builder builder = new RemoteMessage.Builder("TOKEN");
        builder.addData("", "").addData("", "");
        notificationInstance.send(builder.build());
    }

    private MessagesList messagesList;
    private static final String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common4_chat_activity);
        setActivityContext(this, fromFlag);
        mModel = new ChatActivityModel(this);
        mPresenter = new ChatActivityPresenter(this, mModel);
        mPresenter.chatEnqueue();
        configureAppbarWithoutColloapsing(this, " ");
        //findLoadView
        google = Google.getInstance();
        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        this.messagesList.removeAllViews();

        initAdapter();
        if (!this.messagesAdapter.isEmpty()) {
            this.messagesAdapter.clear();
        }
        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
        input.setAttachmentsListener(this);
        Google google = Google.getInstance();
        int i = google.getRoomIdList().indexOf(google.getCurrentRoom());
        title.setText(google.getRooms().get(i).getOpponentName());
        switch (google.getAccountMajor()) {
            case DumeUtils.TEACHER:
                name = TeacherDataStore.getInstance().gettUserName();
                avatar = TeacherDataStore.getInstance().gettAvatarString();
                break;
            case DumeUtils.STUDENT:
                name = SearchDataStore.getInstance().getUserName();
                avatar = SearchDataStore.getInstance().getAvatarString();
                break;
            default://this is mainly the boot camp
                name = SearchDataStore.getInstance().getUserName();
                avatar = SearchDataStore.getInstance().getAvatarString();
                break;
        }
        Glide.with(context).load(google.getRooms().get(i).getOpponentDP()).apply(new RequestOptions().placeholder(R.drawable.demo_default_avatar_dark)).into(typingAvatar);
        mModel.getToken(google.getRooms().get(i).getOpponentUid()
                , new TeacherContract.Model.Listener<String>() {
                    @Override
                    public void onSuccess(String list) {
                        token = list;
                    }

                    @Override
                    public void onError(String msg) {
                        token = null;
                        flush(msg);
                    }
                });
        Glide.with(this).load(google.getRooms().get(i).getOpponentDP()).apply(new RequestOptions().placeholder(R.drawable.dp)).into(dp);

        mModel.readLastThirtyOnce(new TeacherContract.Model.Listener<List<Letter>>() {
            @Override
            public void onSuccess(List<Letter> list) {
                List<Message> messages = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getUid().equals(FirebaseAuth.getInstance().getUid())) {
                        TYPE = SENDER;
                    } else {
                        TYPE = RECIVER;
                    }
                    Message message = new Message(list.get(i).getUid(), new User(TYPE + "", list.get(i).getName(), list.get(i).getAvatar(), true), list.get(i).getBody(), list.get(i).getTimestamp());
                    messages.add(message);
                    Log.w(TAG, "tttt " + list.get(i).getBody());
                }
                Log.w(TAG, "onSuccess: To test " + messages.size());
                loadingProgressBar.setVisibility(View.GONE);
                ChatActivity.super.messagesAdapter.addToEnd(messages, false);

                if (list.size() > 0) {
                    Google.getInstance().setLastDocumentOfMessage(list.get(list.size() - 1).getDoc());
                }
                if (list.size() == 30) {
                    isDataExists = true;
                }
                if (list.size() <= 0) {
                    noDataBlock.setVisibility(View.VISIBLE);
                } else {
                    noDataBlock.setVisibility(View.GONE);
                }

                mModel.onInboxChange(new TeacherContract.Model.Listener<Letter>() {
                    @Override
                    public void onSuccess(Letter letter) {
                        if (letter.getUid().equals(FirebaseAuth.getInstance().getUid())) {
                            TYPE = SENDER;
                        } else {
                            TYPE = RECIVER;
                        }
                        /*(google.getSnapCounter() == 2 && letter.size() != 0 && letter.get(0).getBody().equals(list.get(0).getBody()))*/
                        google.setSnapCounter(google.getSnapCounter() + 1);
                        if (google.getSnapCounter() == 1 && list.size() != 0) {

                        } else if (list.size() > 0 && (google.getSnapCounter() == 2 && letter.getBody().equals(list.get(0).getBody()))) {

                        } else {
                            ChatActivity.super.messagesAdapter
                                    .addToStart(new Message(letter.getUid(), new User("" + TYPE, letter.getName(), letter.getAvatar(), true), letter.getBody(), letter.getTimestamp()), true);
                            if (noDataBlock.getVisibility() == View.VISIBLE) {
                                noDataBlock.setVisibility(View.GONE);
                            }
                        }

                    }

                    @Override
                    public void onError(String msg) {

                    }
                });

            }

            @Override
            public void onError(String msg) {
                flush(msg);
            }
        });


        input.setTypingListener(new MessageInput.TypingListener() {
            @Override
            public void onStartTyping() {
                mModel.onType(true, new TeacherContract.Model.Listener<Void>() {
                    @Override
                    public void onSuccess(Void list) {

                    }

                    @Override
                    public void onError(String msg) {

                    }
                });
            }

            @Override
            public void onStopTyping() {
                mModel.onType(false, new TeacherContract.Model.Listener<Void>() {
                    @Override
                    public void onSuccess(Void list) {

                    }

                    @Override
                    public void onError(String msg) {

                    }
                });
            }
        });

        mModel.onTypeStateChange(new TeacherContract.Model.Listener<Boolean>() {
            @Override
            public void onSuccess(Boolean list) {
                if (list) {
                    hideableTypingRelative.setVisibility(View.VISIBLE);
                } else {
                    hideableTypingRelative.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String msg) {

            }
        });

        super.messagesAdapter.setLoadMoreListener((page, totalItemsCount) -> {
            if (isDataExists) {
                mModel.readLastThirty(Google.getInstance().getLastDocumentOfMessage(), new TeacherContract.Model.Listener<List<Letter>>() {

                    private List<Message> messages;

                    @Override
                    public void onSuccess(List<Letter> list) {
                        messages = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getUid().equals(FirebaseAuth.getInstance().getUid())) {
                                TYPE = SENDER;
                            } else {
                                TYPE = RECIVER;
                            }
                            Message message = new Message(list.get(i).getUid(), new User(TYPE + "", list.get(i).getName(), list.get(i).getAvatar(), true), list.get(i).getBody(), list.get(i).getTimestamp());
                            messages.add(message);
                        }
                        if (list.size() > 0) {
                            Google.getInstance().setLastDocumentOfMessage(list.get(list.size() - 1).getDoc());
                        }
                        isDataExists = list.size() == 30;
                        ChatActivity.super.messagesAdapter.addToEnd(messages, false);
                    }

                    @Override
                    public void onError(String msg) {
                        flush(msg);
                    }
                });

            }
        });

    }

    @Override
    protected void onDestroy() {
        google.setSnapCounter(0);
        super.onDestroy();
    }

    @Override
    public void findView() {
        attachmentHolder = findViewById(R.id.attachment_holder);
        inbetweenDivider = findViewById(R.id.inbetween_divider);
        transitionHostLayout = findViewById(R.id.transitionHostLayout);
        attachmentDocument = findViewById(R.id.attachment_document);
        attachemtDocumentImage = findViewById(R.id.attachment_document_image);
        viewMusk = findViewById(R.id.view_musk);
        dp = findViewById(R.id.noti_user_dp);
        title = findViewById(R.id.noti_user_name);
        input = findViewById(R.id.input);
        typeingTV = findViewById(R.id.isTypingTV);
        loadingProgressBar = findViewById(R.id.loadingPanel);
        progressBar = findViewById(R.id.progress_bar);
        progressBarTypingTwo = findViewById(R.id.typing_progress_bar_one);
        hideableTypingRelative = findViewById(R.id.hideable_retative_typing);
        noDataBlock = findViewById(R.id.no_data_block);
        typingAvatar = findViewById(R.id.messageUserAvatar);
    }


    @Override
    public void initChat() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            StatusBarUtil.setTranslucent(activity, 50);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }


    }

    @Override
    public void configChat() {
        final int min = 1;
        final int max = 3;
        final int random = new Random().nextInt((max - min) + 1) + min;
        Sprite doubleBounce = new DoubleBounce();
        switch (random) {
            case 1:
                doubleBounce = new DoubleBounce();
                break;
            case 2:
                doubleBounce = new ChasingDots();
                break;
            case 3:
                doubleBounce = new Pulse();
                break;
            default:
                doubleBounce = new DoubleBounce();
                break;
        }
        doubleBounce.setColor(getResources().getColor(R.color.inbox_active_color));
        progressBar.setIndeterminateDrawable(doubleBounce);

        Sprite typingSpriteTwo = new ThreeBounce();
        typingSpriteTwo.setColor(getResources().getColor(R.color.inbox_active_color));
        progressBarTypingTwo.setIndeterminateDrawable(typingSpriteTwo);


        /*MessagesListAdapter.HoldersConfig holdersConfig = new MessagesListAdapter.HoldersConfig();
        holdersConfig.setIncomingLayout(R.layout.item_incoming_image_message);
        holdersConfig.setOutcomingLayout(R.layout.my_item_outcoming);
        holdersConfig.setIncomingHolder(CustomIncomingMessageViewHolder.class);
        holdersConfig.setOutcomingHolder(CustomOutcomingMessageViewHolder.class);*/

       /* MessageHolders holdersConfig = new MessageHolders()
                .setIncomingTextConfig(
                        CustomIncomingMessageViewHolder.class,
                        R.layout.item_incoming_image_message)
                .setOutcomingTextConfig(CustomOutcomingMessageViewHolder.class,
                        R.layout.my_item_outcoming);

        this.messagesAdapter = new MessagesListAdapter<>(senderId, holdersConfig, imageLoader);*/
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals("")) {
            Letter letter = new Letter(FirebaseAuth.getInstance().getUid(), input.toString(), new Date());
            letter.setToken(token);
            letter.setName(name);
            letter.setAvatar(avatar);
            mModel.addMessage(Google.getInstance().getCurrentRoom(), letter, new TeacherContract.Model.Listener<Void>() {
                @Override
                public void onSuccess(Void list) {
                    /*ChatActivity.super.messagesAdapter.addToStart(
                            MessagesFixtures.getTextMessage(input.toString()), true);*/
                }

                @Override
                public void onError(String msg) {
                    flush(msg);
                }
            });


        } else {
            flush("Session Expired. Please Login Again");
        }

        return true;
    }

    public void flush(String toFlush) {
        Toast toast = Toast.makeText(this, toFlush, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public void onAddAttachments() {
        //testing code goes here
        TransitionSet set = new TransitionSet()
                .addTransition(new Fade())
                .addTransition(new Slide(Gravity.BOTTOM))
                .setInterpolator(new LinearOutSlowInInterpolator())
                .addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(@NonNull Transition transition) {

                    }

                    @Override
                    public void onTransitionEnd(@NonNull Transition transition) {
                        if (attachmentHolder.getVisibility() == View.INVISIBLE) {
                            attachmentHolder.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onTransitionCancel(@NonNull Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(@NonNull Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(@NonNull Transition transition) {

                    }
                });
        TransitionManager.beginDelayedTransition(transitionHostLayout, set);
        if (attachmentHolder.getVisibility() == View.VISIBLE) {
            attachmentHolder.setVisibility(View.INVISIBLE);
            viewMusk.setVisibility(View.INVISIBLE);
            inbetweenDivider.setVisibility(View.VISIBLE);
        } else {
            attachmentHolder.setVisibility(View.VISIBLE);
            viewMusk.setVisibility(View.VISIBLE);
            inbetweenDivider.setVisibility(View.GONE);
        }
    }

    @Override
    public void showDialogue() {
        new AlertDialog.Builder(this)
                .setItems(R.array.view_types_dialog, this)
                .show();
    }

    @Override
    public void viewMuskClicked() {
        onAddAttachments();
    }

    @Override
    public void comingSoon() {
        flush("This feature is not available right now...[Coming soon]");
    }

    @Override
    public boolean hasContentFor(Message message, byte type) {
        switch (type) {
            case CONTENT_TYPE_VOICE:
                return message.getVoice() != null
                        && message.getVoice().getUrl() != null
                        && !message.getVoice().getUrl().isEmpty();
        }
        return false;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case 0:
                messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true);
                break;
            case 1:
                messagesAdapter.addToStart(MessagesFixtures.getVoiceMessage(), true);
                break;
        }
    }

    private void initAdapter() {
        MessageHolders holders = new MessageHolders()
                .registerContentType(
                        CONTENT_TYPE_VOICE,
                        IncomingVoiceMessageViewHolder.class,
                        R.layout.item_custom_incoming_voice_message,
                        OutcomingVoiceMessageViewHolder.class,
                        R.layout.item_custom_outcoming_voice_message,
                        this)
                .setIncomingTextConfig(
                        CustomIncomingMessageViewHolder.class,
                        R.layout.my_item_incoming)
                .setOutcomingTextConfig(CustomOutcomingMessageViewHolder.class,
                        R.layout.my_item_outcoming);


        super.messagesAdapter = new MessagesListAdapter<>(super.senderId, holders, super.imageLoader);
        super.messagesAdapter.enableSelectionMode(this);
        super.messagesAdapter.setLoadMoreListener(this);
        this.messagesList.setAdapter(super.messagesAdapter);
    }

    public void onChatActivityClicked(View view) {
        mPresenter.onChatViewIntracted(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
