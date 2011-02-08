package cn.edu.hit.voidmain.asmsreplier.activities.lists.itemview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import cn.edu.hit.voidmain.asmsreplier.R;
import cn.edu.hit.voidmain.asmsreplier.activities.lists.data.ToReplyData;

public class ListToReplyItemView extends TableLayout {
	private TextView tvTargetName;
	private TextView tvRespondTime;
	
	private Context context;
	
	private ToReplyData data;

	public ListToReplyItemView(Context context) {
		super(context);
		
		this.context = context;
		
		initialize(context);
	}

	private void initialize(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.listview_to_reply_item_layout, null);
		tvTargetName = (TextView)view.findViewById(R.id.to_reply_target_name);
		tvRespondTime = (TextView)view.findViewById(R.id.to_reply_respond_time);
		addView(view);
	}
	
	public void updateView()
	{
		tvTargetName.setText(String.format(
				context.getResources().getString(R.string.to_reply_content_template), 
				data.getTargetName(), 
				data.getTargetNumber()));
		tvRespondTime.setText(data.getRespondTime());
	}
	
	public ToReplyData getData()
	{
		return data;
	}
	
	public void setData(ToReplyData data)
	{
		this.data = data;
	}
}
