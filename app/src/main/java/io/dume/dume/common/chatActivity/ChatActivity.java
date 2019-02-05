package io.dume.dume.common.chatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.Date;
import java.util.List;

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
import io.dume.dume.teacher.pojo.Letter;

import static io.dume.dume.util.DumeUtils.configureAppbarWithoutColloapsing;

public class ChatActivity extends DemoMessagesActivity implements ChatActivityContact.View,
        MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageHolders.ContentChecker<Message>,
        DialogInterface.OnClickListener {

    private ChatActivityContact.Presenter mPresenter;
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

    public static void open(Context context) {
        context.startActivity(new Intent(context, ChatActivity.class));
    }

    private MessagesList messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common4_chat_activity);
        mModel = new ChatActivityModel(this);
        mPresenter = new ChatActivityPresenter(this, mModel);
        mPresenter.chatEnqueue();
        configureAppbarWithoutColloapsing(this, " ");
        //findLoadView
        this.messagesList = (MessagesList) findViewById(R.id.messagesList);
        initAdapter();

        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
        input.setAttachmentsListener(this);
        Google google = Google.getInstance();
        int i = google.getRoomIdList().indexOf(google.getCurrentRoom());
        title.setText(google.getRooms().get(i).getOpponentName());
        Glide.with(this).load(google.getRooms().get(i).getOpponentDP()).apply(new RequestOptions().placeholder(R.drawable.dp)).into(dp);

        mModel.readLastThirty(new TeacherContract.Model.Listener<List<Letter>>() {
            @Override
            public void onSuccess(List<Letter> list) {
                for (int i = 0; i < list.size() - 1; i++) {
                    if (list.get(i).getUid().equals(FirebaseAuth.getInstance().getUid())) {
                        TYPE = SENDER;
                    } else {
                        TYPE = RECIVER;
                    }
                    ChatActivity.super.messagesAdapter.addToStart(new Message(list.get(i).getUid(), new User(TYPE + "", "Enam", SearchDataStore.DEFAULTMALEAVATER, true), list.get(i).getBody(), list.get(i).getTimestamp()), true);
                }
            }

            @Override
            public void onError(String msg) {
                flush(msg);
            }
        });


        mModel.onInboxChange(new TeacherContract.Model.Listener<List<Letter>>() {
            @Override
            public void onSuccess(List<Letter> list) {

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getUid().equals(FirebaseAuth.getInstance().getUid())) {
                        TYPE = SENDER;
                    } else {
                        TYPE = RECIVER;
                    }
                    ChatActivity.super.messagesAdapter.addToStart(new Message(list.get(i).getUid(), new User("" + TYPE, "Enam", SearchDataStore.DEFAULTMALEAVATER, true), list.get(i).getBody(), list.get(i).getTimestamp()), true);
                }
            }

            @Override
            public void onError(String msg) {

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
                    typeingTV.setVisibility(View.VISIBLE);
                    typeingTV.setText("Foo is Typing...");
                } else {
                    typeingTV.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String msg) {

            }
        });

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
    }

    @Override
    public void initChat() {

    }

    @Override
    public void configChat() {

    }

    @Override
    public boolean onSubmit(CharSequence input) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null && !FirebaseAuth.getInstance().getCurrentUser().getUid().equals("")) {
            Letter letter = new Letter(FirebaseAuth.getInstance().getUid(), input.toString(), new Date());
            mModel.addMessage(Google.getInstance().getCurrentRoom(), letter, new TeacherContract.Model.Listener<Void>() {
                @Override
                public void onSuccess(Void list) {
                    flush("Sent");
                    /*ChatActivity.super.messagesAdapter.addToStart(
                            MessagesFixtures.getTextMessage(input.toString()), true);*/
                }

                @Override
                public void onError(String msg) {

                }
            });


        } else {
            flush("Session Expired. Please Login Again");
        }

        return true;
    }

    public void flush(String toFlush) {
        Toast.makeText(this, toFlush, Toast.LENGTH_SHORT).show();
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
                        this);


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
