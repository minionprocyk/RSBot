package procyk.industries.rt6.actions;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;

import procyk.industries.rt6.localplayer.Backpack;
import procyk.industries.shared.constants.InteractConstants;
import procyk.industries.shared.constants.WidgetIdConstantsRT6;

public class GrandExchange {
	public static boolean buy(ClientContext ctx, String itemName)
	{
		Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE,WidgetIdConstantsRT6.GRAND_EXCHANGE_BUY1);
		
		return false;
		
	}
	public static boolean sell(ClientContext ctx, String itemName, Price price, Quantity quantity)
	{
		if(sellWindowOpen(ctx))
		{
			//post the item
			Backpack.Use(ctx, itemName, InteractConstants.OFFER);
			//set the quantity
			switch(quantity)
			{
			case ALL:
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL_SET_QUANTITY_ALL);
				break;
			case HUNDRED:
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL_SET_QUANTITY_100);
				break;
			case TEN:
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL_SET_QUANTITY_10);
				break;
			case ONE:
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL_SET_QUANTITY_1);
				break;
			}
			//set price
			switch(price)
			{
			case GUIDE:
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL_SET_GUIDE_PRICE);
				break;
			case FIVE_LOW:
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL_MINUS_5_PERCENT);
				break;
			case TEN_LOW:
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL_MINUS_5_PERCENT);
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL_MINUS_5_PERCENT);
				break;
			case FIVE_HIGH:
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL_PLUS_5_PERCENT);
				break;
			case TEN_HIGH:
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL_PLUS_5_PERCENT);
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL_PLUS_5_PERCENT);
				break;
			}
			//confirm the offer
			Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_CONFIRM_OFFER);
		}
		else
		{
			openSellWindow(ctx);
		}
		return false;
	}
	public static void collect(ClientContext ctx)
	{
		if(isOpen(ctx))
		{
			//check if there is a completed progress bar to collect an item
			//new line
		}
	}
	public static void openSellWindow(ClientContext ctx)
	{
		Integer[] sellButtons = new Integer[]{WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL1,WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL2,WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL3,
				WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL4,WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL5,WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL6,WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL7,WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL8};
		if(isOpen(ctx))
		{
			//if we're on the buy or sell window. go back
			if(ctx.widgets.select().id(WidgetIdConstantsRT6.GRAND_EXCHANGE).poll().component(WidgetIdConstantsRT6.GRAND_EXCHANGE_BACK_BUTTON).visible())
			{
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_BACK_BUTTON);
			}
			//if were at the main window. find a button we didn't already use and use that
			for(Integer buttons:sellButtons)
			{
				if(ctx.widgets.select().id(WidgetIdConstantsRT6.GRAND_EXCHANGE).poll().component(buttons).visible())
				{
					Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE,buttons);
					break;
				}
			}
		}
		else
		{
			System.out.println("Grand Exchange is not open. Cannot perform operation");
		}
		throw new NullPointerException("Cannot open sell window");
		
	}
	private static void openBuyWindow(ClientContext ctx)
	{
		Integer[] buyButtons = new Integer[]{WidgetIdConstantsRT6.GRAND_EXCHANGE_BUY1,WidgetIdConstantsRT6.GRAND_EXCHANGE_BUY2,WidgetIdConstantsRT6.GRAND_EXCHANGE_BUY3,
				WidgetIdConstantsRT6.GRAND_EXCHANGE_BUY4,WidgetIdConstantsRT6.GRAND_EXCHANGE_BUY5,WidgetIdConstantsRT6.GRAND_EXCHANGE_BUY6,WidgetIdConstantsRT6.GRAND_EXCHANGE_BUY7,WidgetIdConstantsRT6.GRAND_EXCHANGE_BUY8};
		
		if(isOpen(ctx))
		{
			//if we're on the buy or sell window. go back
			if(ctx.widgets.select().id(WidgetIdConstantsRT6.GRAND_EXCHANGE).poll().component(WidgetIdConstantsRT6.GRAND_EXCHANGE_BACK_BUTTON).valid())
			{
				Widgets.Click(ctx, WidgetIdConstantsRT6.GRAND_EXCHANGE, WidgetIdConstantsRT6.GRAND_EXCHANGE_BACK_BUTTON);
			}
			
			//if were at the main window. find a button we didnt already use and use that
			for(Integer buttons:buyButtons)
			{
				if(ctx.widgets.select().id(WidgetIdConstantsRT6.GRAND_EXCHANGE).poll().component(buttons).valid())
				{
					Widgets.Click(ctx, buttons);
					break;
				}
			}
		}
		else
		{
			System.out.println("Grand Exchange is not open. Cannot perform operation");
		}
		throw new NullPointerException("Cannot open buy window");
	}
	public static void TestWindowOpen(ClientContext ctx)
	{
		buyWindowOpen(ctx);
		sellWindowOpen(ctx);
		isOpen(ctx);
	}
	private static boolean buyWindowOpen(ClientContext ctx)
	{
		if(ctx.widgets.select().id(WidgetIdConstantsRT6.GRAND_EXCHANGE).poll().component(WidgetIdConstantsRT6.GRAND_EXCHANGE_BUY_SEARCH).visible())
		{
			System.out.println("buy window is open");
			return true;
		}
		else
		{
			return false;
		}
	}
	private static boolean sellWindowOpen(ClientContext ctx)
	{
		if(ctx.widgets.select().id(WidgetIdConstantsRT6.GRAND_EXCHANGE).poll().component(WidgetIdConstantsRT6.GRAND_EXCHANGE_SELL_SELECT_ITEM_TO_SELL).visible())
		{
			System.out.println("sell window is open");
			return true;
		}
		else
		{
			return false;
		}
	}
	private static boolean isOpen(ClientContext ctx)
	{
		if(ctx.widgets.select().id(WidgetIdConstantsRT6.GRAND_EXCHANGE).poll().valid())
		{
			System.out.println("Grand Exchange is open");
			return true;
		}
		else
		{
			return false;
		}
	}
	public static int GetPrice(Integer itemId)
	{
		GeItem geItem = GeItem.profile(itemId);
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
