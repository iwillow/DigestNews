package iwillow.com.gaodemap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExpandListViewActivity extends AppCompatActivity {
    private final String TAG = ExpandListViewActivity.class.getSimpleName();
    private ExpandableListViewEx expandableListView;
    private List<GroupItem> groupItems = new ArrayList<>();
    private MyExpandAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_list_view);
        expandableListView = (ExpandableListViewEx) findViewById(R.id.expand_list_view);
        expandableListView.setOnHeaderUpdateListener(new ExpandableListViewEx.OnHeaderUpdateListener() {
            @Override
            public View getPinnedHeader() {
               View  pinHeaderView = LayoutInflater.from(expandableListView.getContext()).inflate(R.layout.pin_header, null);
                pinHeaderView.setLayoutParams(new AbsListView.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
                return pinHeaderView;
            }

            @Override
            public void updatePinnedHeader(View headerView, int firstVisibleGroupPosition, int firstVisibleChildPosition) {
                if (headerView != null && headerView.findViewById(R.id.pin_header) != null && adapter != null) {
                    ((TextView) headerView.findViewById(R.id.pin_header)).setText("" + groupItems.get(firstVisibleGroupPosition).group);
                }
            }
        });
        initItems();
    }

    private void initItems() {
        groupItems.clear();
        for (int i = 1; i <= 5; i++) {
            List<String> list = new ArrayList<>();
            for (int j = 1; j <= 10; j++) {
                list.add(String.valueOf("parent" + i + ";child:" + j));
            }
            GroupItem groupItem = new GroupItem(String.valueOf("parent" + i), list);
            groupItems.add(groupItem);
        }
        adapter = new MyExpandAdapter(groupItems);
        expandableListView.setAdapter(adapter);
    }

    private static class MyExpandAdapter extends BaseExpandableListAdapter {
        private List<GroupItem> groupItems;

        public MyExpandAdapter(List<GroupItem> groupItems) {
            this.groupItems = groupItems;
        }

        @Override
        public int getGroupCount() {
            return groupItems == null ? 0 : groupItems.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groupItems == null ? 0 : groupItems.get(groupPosition) == null ? 0 : groupItems.get(groupPosition).getChildren().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupItems == null ? null : groupItems.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if (groupItems != null && groupItems.get(groupPosition) != null) {
                return groupItems.get(groupPosition).getChildren().get(childPosition);
            }

            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder groupHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
                groupHolder = new GroupHolder(convertView);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }
            GroupItem item = (GroupItem) getGroup(groupPosition);
            groupHolder.textView.setText("" + item.group);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder childHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item, parent, false);
                childHolder = new ChildHolder(convertView);
                convertView.setTag(childHolder);
            } else {
                childHolder = (ChildHolder) convertView.getTag();
            }
            String child = (String) getChild(groupPosition, childPosition);
            childHolder.textView.setText("" + child);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        static class GroupHolder {
            public final View itemView;
            public final TextView textView;

            public GroupHolder(View itemView) {
                this.itemView = itemView;
                textView = (TextView) itemView.findViewById(R.id.text_view_group);
            }


        }

        static class ChildHolder {
            public final View itemView;
            public final TextView textView;

            public ChildHolder(View itemView) {
                this.itemView = itemView;
                textView = (TextView) itemView.findViewById(R.id.text_view_child);
            }
        }
    }

    public static class GroupItem {
        private final String group;
        private final List<String> children;

        public GroupItem(String group, List<String> children) {
            this.group = group;
            this.children = children;
        }

        public String getGroup() {
            return group;
        }

        public List<String> getChildren() {
            return children;
        }
    }

}
