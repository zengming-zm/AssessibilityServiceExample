package com.example.assessibilityserviceexample;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Pair;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityWindowInfo;
import android.view.accessibility.AccessibilityNodeInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExampleAccessibilityService extends AccessibilityService {

    static final String TAG = "DEBUG";

    ArrayList<AccessibilityNodeInfo> textViewNodes;
    ArrayList<Integer> textViewNodeIndex;
    Pair<AccessibilityNodeInfo, Integer> p;
    ArrayList<Pair<AccessibilityNodeInfo, Integer>> textPairViewNodes;

    AccessibilityNodeInfo currNode;
    String updateType = "";
    String timestamp;
    public static JSONObject obj;
//    public static JSONObject head;
    public static JSONArray jsonList = new JSONArray();

    JSONObject outputLayout = new JSONObject();


    private String getEventTypeString(int eventType) {
        switch (eventType) {
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                return "TYPE_ANNOUNCEMENT";
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
                return "TYPE_GESTURE_DETECTION_END";
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
                return "TYPE_GESTURE_DETECTION_START";
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                return "TYPE_NOTIFICATION_STATE_CHANGED";
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                return "TYPE_TOUCH_EXPLORATION_GESTURE_END";
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                return "TYPE_TOUCH_EXPLORATION_GESTURE_START";
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
                return "TYPE_TOUCH_INTERACTION_END";
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
                return "TYPE_TOUCH_INTERACTION_START";
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                return "TYPE_VIEW_ACCESSIBILITY_FOCUSED";
            case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
                return "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED";
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                return "TYPE_VIEW_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                return "TYPE_VIEW_FOCUSED";
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                return "TYPE_VIEW_HOVER_ENTER";
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                return "TYPE_VIEW_HOVER_EXIT";
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                return "TYPE_VIEW_LONG_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                return "TYPE_VIEW_SCROLLED";
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                return "TYPE_VIEW_SELECTED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                return "TYPE_VIEW_TEXT_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                return "TYPE_VIEW_TEXT_SELECTION_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY:
                return "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY";
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                return "TYPE_WINDOWS_CHANGED";
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                return "TYPE_WINDOW_CONTENT_CHANGED";
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                return "TYPE_WINDOW_STATE_CHANGED";
        }
        return String.format("unknown (%d)", eventType);
    }

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    private void dumpNode(AccessibilityNodeInfo node, int indent) {
        if (node == null) {
            Log.v(TAG, "node is null (stopping iteration)");
            return;
        }

        String indentStr = new String(new char[indent * 3]).replace('\0', ' ');
        Log.v(TAG, String.format("%s NODE: %s", indentStr, node.toString()));
        for (int i = 0; i < node.getChildCount(); i++) {
            dumpNode(node.getChild(i), indent + 1);
        }
        /* NOTE: Not sure if this is really required. Documentation is unclear. */
        node.recycle();
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        /* Show the accessibility event */
//        Log.v(TAG, String.format(
//                "onAccessibilityEvent: [type] %s [class] %s [package] %s [time] %s [text] %s",
//                getEventTypeString(event.getEventType()), event.getClassName(), event.getPackageName(),
//                event.getEventTime(), getEventText(event)));

//        Log.d("tv1Text" , getEventTypeString(event.getEventType()));



        /* Show all the windows available */
        List<AccessibilityWindowInfo> windows = getWindows();
        Log.v(TAG, String.format("Windows (%d):", windows.size()));
        for (AccessibilityWindowInfo window : windows) {
            Log.v(TAG, String.format("window: %s", window.toString()));
//            Log.v(TAG, "window content: " + window.);
        }
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        textViewNodes = new ArrayList<AccessibilityNodeInfo>();
        for (AccessibilityNodeInfo mNode : textViewNodes) {
            if (mNode.getText() != null) {
                Log.d(TAG, "window content: " + mNode.getText().toString());
            }
        }

        Log.d("DEBUG", "get event type: " + getEventTypeString(event.getEventType()));

        int eventType = event.getEventType();

        timestamp = String.valueOf(System.currentTimeMillis());
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.d(TAG, "======tv1Text: TYPE_VIEW_CLICKED begin========");
                updateType = "TYPE_VIEW_CLICKED";
                rootNode = event.getSource();

                if (rootNode == null || rootNode.getPackageName() == null ){
                    break;
                }
//                if (!rootNode.getPackageName().toString().contentEquals("com.google.android.googlequicksearchbox")) {
//                    break;
//                }

//                int windowId = event.getWindowId();
                Log.d("tv1Text", "event.getSource()" + rootNode.getClassName().toString());
                Rect rect = new Rect();
                rootNode.getBoundsInScreen(rect);

                Log.d(TAG, String.format("tv1Text: clicked bounding box: top:%d, bottom:%d, left:%d, right:%d: "
                        , rect.top
                        , rect.bottom
                        , rect.left
                        , rect.right));


//                findSourceWindow2(rootNode, windowId, 0);

//                AccessibilityNodeInfo rootNode1 = getRootInActiveWindow();
                obj = new JSONObject();

                long startTimestamp = System.currentTimeMillis();

                JSONObject head = findChildViews2(currNode, 0, rect);
                if (head == null) {
                    break;
                }

                try {
                    head.put("timestamp", String.valueOf(startTimestamp));
                    head.put("update_type", "TYPE_VIEW_CLICKED");
                }catch (JSONException e){
                    Log.d("tv1Text", "input to json error: " + e.toString());
                    e.printStackTrace();
                }




                Log.d("tv1Text", "head.size: " + String.valueOf(head.length()));
                Log.d("tv1Text", "head: " + head.toString());

                String filename = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/test/" + timestamp + ".json";
                try {
                    Log.d("tv1Text", "save to json");
                    ObjectOutputStream outputStream = null;
                    outputStream = new ObjectOutputStream(new FileOutputStream(filename));
                    outputStream.writeObject(head.toString());
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    Log.d("tv1Text", "output error: " + e.toString());
                    System.err.println("Error: " + e);
                }


                Log.d(TAG, "======tv1Text: TYPE_VIEW_CLICKED end========");
//                findChildViews(rootNode, 0);
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                Log.d(TAG, "tv1Text: TYPE_VIEW_SCROLLED");
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                updateType = "TYPE_WINDOW_CONTENT_CHANGED";
                rootNode = getRootInActiveWindow();
                currNode = rootNode;

                if (rootNode == null || rootNode.getPackageName() == null ){
                    return;
                }

                textViewNodes = new ArrayList<AccessibilityNodeInfo>();

                if (rootNode.getPackageName().toString().contentEquals("com.google.android.googlequicksearchbox")) {

                    Log.d(TAG, "tv1Text: ===================Log Begin===================");

                    obj = new JSONObject();

                    startTimestamp = System.currentTimeMillis();
                    head = findChildViews2(rootNode, 0, null);

                    if (head == null) {
                        break;
                    }

                    try {
                        head.put("timestamp", String.valueOf(startTimestamp));
                        head.put("update_type", "TYPE_WINDOW_CONTENT_CHANGED");
                    }catch (JSONException e){
                        Log.d("tv1Text", "input to json error: " + e.toString());
                        e.printStackTrace();
                    }


                    Log.d("tv1Text", "head.size: " + String.valueOf(head.length()));
                    Log.d("tv1Text", "head: " + head.toString());

                    Log.d(TAG, "tv1Text: ===================Log end===================");

                    filename = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/test/" + timestamp + ".json";
                    try {
                        Log.d("tv1Text", "save to json");
                        ObjectOutputStream outputStream = null;
                        outputStream = new ObjectOutputStream(new FileOutputStream(filename));
                        outputStream.writeObject(head.toString());
                        outputStream.flush();
                        outputStream.close();

                    } catch (Exception e) {
                        Log.d("tv1Text", "output error: " + e.toString());
                        System.err.println("Error: " + e);
                    }
                }
                break;
        }

    }

    private JSONObject findChildViews2(AccessibilityNodeInfo parentView, int j, Rect clickBox) {
        if (parentView == null || parentView.getClassName() == null) {
            Log.d("tv1Text", "in findChildViews, parentView == null || parentView.getClassName()");
            return null;
        }


        String className =
                parentView.getClassName() == null ? "" : parentView.getClassName().toString();
        String resourceName =
                parentView.getViewIdResourceName() == null ? "" : parentView.getViewIdResourceName();
        String content = "";
        if (parentView.getClassName() != null && parentView.getText() != null && parentView.getClassName().toString().contains("TextView")){
            content = parentView.getText().toString();
        }

        int layer = j;

        Rect rect = new Rect();
        Rect parentRect = new Rect();
        parentView.getBoundsInScreen(rect);
        parentView.getBoundsInParent(parentRect);

        Log.d(TAG, String.format("tv1Text: " +
                        "class_name:[%s], resource_name:[%s], content:[%s], layer:[%d]" +
                        ", top:[%d], bottom:[%d], left:[%d], right:[%d]" +
                        ", parTop:[%d], parBottom:[%d],parLeft:[%d],parRight:[%d]"
                , className
                , resourceName
                , content
                , layer
                , rect.top
                , rect.bottom
                , rect.left
                , rect.right
                , parentRect.top
                , parentRect.bottom
                , parentRect.left
                , parentRect.right));
        //com.google.android.apps.gsa.searchplate.widget.StreamingTextView
        //android.widget.TextView

        JSONObject obj = new JSONObject();
        try {
            obj.put("class_name", className);
            obj.put("resource_name", resourceName);
            obj.put("content", content);
            obj.put("layer", String.valueOf(layer));

            JSONObject jRect = new JSONObject();
            jRect.put("top", String.valueOf(rect.top));
            jRect.put("bottom", String.valueOf(rect.bottom));
            jRect.put("left", String.valueOf(rect.left));
            jRect.put("right", String.valueOf(rect.right));

            obj.put("rect", jRect);

            if (clickBox != null
                    && clickBox.top == rect.top
                    && clickBox.bottom  == rect.bottom
                    && clickBox.left == rect.left
                    && clickBox.right == rect.right){
                obj.put("is_source", 1);
            }

//            if (layer == 0) {
//                Log.d("tv1Text", "layer 0");
//                obj.put("time", timestamp);
//                obj.put("update_type", updateType);
//            }

        } catch (JSONException e) {
            Log.d("tvText", "json error: " + e.toString());
            e.printStackTrace();
        }

        int childCount = parentView.getChildCount();
        if (childCount == 0) {
            return obj;
        }

//        List<JSONObject> children = new ArrayList<JSONObject>();
        JSONObject children = new JSONObject();

        try {
            for (int i = 0; i < childCount; i++) {
                children.put("child(" + String.valueOf(i) + ")", findChildViews2(parentView.getChild(i), j + 1, clickBox));
            }
            obj.put("child", children);
        } catch (JSONException e) {
            Log.d("tvText", "json error: " + e.toString());
            e.printStackTrace();
        }
        return obj;
    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.v(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT |
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS |
                AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY |
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
//        info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
    }
}