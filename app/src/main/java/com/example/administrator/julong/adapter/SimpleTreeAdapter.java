package com.example.administrator.julong.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.julong.R;
import com.example.administrator.julong.entity.Node;
import com.example.administrator.julong.util.Bitmaputil;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T>
{

	public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException
	{
		super(mTree, context, datas, defaultExpandLevel);
	}

	@Override
	public View getConvertView(Node node , int position, View convertView, ViewGroup parent)
	{
		
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_tree, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.iv_icon);
			viewHolder.label = (TextView) convertView
					.findViewById(R.id.tv_fname);
			viewHolder.fimage = (ImageView) convertView
					.findViewById(R.id.iv_fimage);
			convertView.setTag(viewHolder);

		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (node.getIcon() == -1)
		{
			viewHolder.icon.setVisibility(View.INVISIBLE);
		} else
		{
			viewHolder.icon.setVisibility(View.VISIBLE);
			viewHolder.icon.setImageResource(node.getIcon());
		}
		viewHolder.label.setText(node.getName());
		if(node.getFimage()!=null) {
			viewHolder.fimage.setImageBitmap(Bitmaputil.base64ToBitmap(node.getFimage()));
		}
		
		return convertView;
	}

	private final class ViewHolder
	{
		ImageView icon,fimage;
		TextView label;
	}

}
