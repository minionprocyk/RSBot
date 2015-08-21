package Actions;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;

import Constants.ItemId;
import Constants.WidgetId;
import Constants.Interact;

public class GrandExchange {
	public static boolean buy(ClientContext ctx, String itemName)
	{
		Actions.Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE,WidgetId.GRAND_EXCHANGE_BUY1);
		
		return false;
		
	}
	public static boolean sell(ClientContext ctx, String itemName, Price price, Quantity quantity)
	{
		if(sellWindowOpen(ctx))
		{
			//post the item
			LocalPlayer.Backpack.Use(ctx, itemName, Interact.OFFER);
			
			//set the quantity
			switch(quantity)
			{
			case ALL:
				Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_SELL_SET_QUANTITY_ALL);
				break;
			case HUNDRED:
				Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_SELL_SET_QUANTITY_100);
				break;
			case TEN:
				Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_SELL_SET_QUANTITY_10);
				break;
			case ONE:
				Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_SELL_SET_QUANTITY_1);
				break;
			}
			//set price
			switch(price)
			{
			case GUIDE:
				Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_SELL_SET_GUIDE_PRICE);
				break;
			case FIVE_LOW:
				Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_SELL_MINUS_5_PERCENT);
				break;
			case TEN_LOW:
				Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_SELL_MINUS_5_PERCENT);
				Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_SELL_MINUS_5_PERCENT);
				break;
			case FIVE_HIGH:
				Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_SELL_PLUS_5_PERCENT);
				break;
			case TEN_HIGH:
				Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_SELL_PLUS_5_PERCENT);
				Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_SELL_PLUS_5_PERCENT);
				break;
			}
			//confirm the offer
			Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_CONFIRM_OFFER);
		}
		else
		{
			openSellWindow(ctx);
		}
		return false;
	}
	private static void openSellWindow(ClientContext ctx)
	{
		Integer[] sellButtons = new Integer[]{WidgetId.GRAND_EXCHANGE_SELL1,WidgetId.GRAND_EXCHANGE_SELL2,WidgetId.GRAND_EXCHANGE_SELL3,
				WidgetId.GRAND_EXCHANGE_SELL4,WidgetId.GRAND_EXCHANGE_SELL5,WidgetId.GRAND_EXCHANGE_SELL6,WidgetId.GRAND_EXCHANGE_SELL7,WidgetId.GRAND_EXCHANGE_SELL8};
		
		//if we're on the buy or sell window. go back
		if(ctx.widgets.select().id(WidgetId.GRAND_EXCHANGE).poll().component(WidgetId.GRAND_EXCHANGE_BACK_BUTTON).valid())
		{
			Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_BACK_BUTTON);
		}
		//if were at the main window. find a button we didnt already use and use that
		for(Integer buttons:sellButtons)
		{
			if(ctx.widgets.select().id(WidgetId.GRAND_EXCHANGE).poll().component(buttons).valid())
			{
				Widgets.Click(ctx, buttons);
				break;
			}
		}
		
	}
	private static void openBuyWindow(ClientContext ctx)
	{
		Integer[] buyButtons = new Integer[]{WidgetId.GRAND_EXCHANGE_BUY1,WidgetId.GRAND_EXCHANGE_BUY2,WidgetId.GRAND_EXCHANGE_BUY3,
				WidgetId.GRAND_EXCHANGE_BUY4,WidgetId.GRAND_EXCHANGE_BUY5,WidgetId.GRAND_EXCHANGE_BUY6,WidgetId.GRAND_EXCHANGE_BUY7,WidgetId.GRAND_EXCHANGE_BUY8};
		
		//if we're on the buy or sell window. go back
		if(ctx.widgets.select().id(WidgetId.GRAND_EXCHANGE).poll().component(WidgetId.GRAND_EXCHANGE_BACK_BUTTON).valid())
		{
			Widgets.Click(ctx, WidgetId.GRAND_EXCHANGE_BACK_BUTTON);
		}
		
		//if were at the main window. find a button we didnt already use and use that
		for(Integer buttons:buyButtons)
		{
			if(ctx.widgets.select().id(WidgetId.GRAND_EXCHANGE).poll().component(buttons).valid())
			{
				Widgets.Click(ctx, buttons);
				break;
			}
		}
		
	}
	private static boolean buyWindowOpen(ClientContext ctx)
	{
		if(ctx.widgets.select().id(WidgetId.GRAND_EXCHANGE).poll().component(WidgetId.GRAND_EXCHANGE_BUY_SEARCH).valid())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	private static boolean sellWindowOpen(ClientContext ctx)
	{
		if(ctx.widgets.select().id(WidgetId.GRAND_EXCHANGE).poll().component(WidgetId.GRAND_EXCHANGE_SELL_SELECT_ITEM_TO_SELL).valid())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	private static boolean isOpen(ClientContext ctx)
	{
		if(ctx.widgets.select().id(WidgetId.GRAND_EXCHANGE).poll().valid())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static int GetPrice(Integer itemId)
	{
		GeItem geItem = GeItem.profile(ItemId.BRONZE_BAR);
		System.out.println("Price for "+geItem.name()+": "+geItem.price(GeItem.PriceType.CURRENT).price());
		return geItem.price(GeItem.PriceType.CURRENT).price();
	}
	public enum Quantity
	{
		ALL,HUNDRED,TEN,ONE
	}
	public enum Price
	{
		GUIDE,FIVE_LOW, TEN_LOW, FIVE_HIGH, TEN_HIGH
	}

}
