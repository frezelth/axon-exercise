package eu.europa.ec.cc.academy.domain;

import eu.europa.ec.cc.academy.api.command.*;
import eu.europa.ec.cc.academy.api.event.*;
import eu.europa.ec.cc.academy.domain.exception.*;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@NoArgsConstructor
@Aggregate
@SuppressWarnings("unused")
public class TabAR {

  @AggregateIdentifier
  private UUID tabId;

  private Map<Integer, OrderedItem> outstandingDrinks = new HashMap<>();
  private Map<Integer, OrderedItem> outstandingFood = new HashMap<>();
  private Map<Integer, OrderedItem> preparedFood = new HashMap<>();

  private int tableNumber;

  private String waiter;

  private boolean opened;

  private BigDecimal servedItemsValue = new BigDecimal(0);

  @CommandHandler
  public TabAR(OpenTab command){
    AggregateLifecycle.apply(
        TabOpened.builder()
          .tabId(command.getTabId())
          .tableNumber(command.getTableNumber())
          .waiter(command.getWaiter())
          .build()
    );
  }

  @EventSourcingHandler
  void on(TabOpened event){
    this.tabId = event.getTabId();
    this.tableNumber = event.getTableNumber();
    this.waiter = event.getWaiter();
    this.opened = true;
  }

  public boolean isTabOpened(){
    return opened;
  }

  @CommandHandler
  public void placeOrder(PlaceOrder command){
    if (!isTabOpened()){
      throw new TabNotOpenException();
    }

    List<OrderedItemVO> drinks = command.getItems().stream()
        .filter(OrderedItemVO::isDrink)
        .collect(Collectors.toList());

    List<OrderedItemVO> food = command.getItems().stream()
        .filter(oi -> !oi.isDrink())
        .collect(Collectors.toList());

    if (drinks.size() > 0){
      AggregateLifecycle.apply(
          DrinksOrdered.builder()
            .tabId(command.getTabId())
            .items(drinks)
            .build()
      );
    }

    if (food.size() > 0){
      AggregateLifecycle.apply(
          FoodOrdered.builder()
              .tabId(command.getTabId())
              .items(food)
              .build()
      );
    }

  }

  @EventSourcingHandler
  void on(FoodOrdered event){
    List<OrderedItem> list = OrderedItem.from(event.getItems());
    outstandingFood.putAll(list.stream().collect(Collectors.toMap(OrderedItem::getItem, item -> item)));
  }

  @EventSourcingHandler
  void on(DrinksOrdered event){
    List<OrderedItem> list = OrderedItem.from(event.getItems());
    outstandingDrinks.putAll(list.stream().collect(Collectors.toMap(OrderedItem::getItem, item -> item)));
  }

    @CommandHandler
    public void markDrinkServed(MarkDrinkServed command){
        if (!areDrinksOutstanding(command.getItems())){
            throw new DrinksNotOutstandingException();
        }

        AggregateLifecycle.apply(
                DrinksServed.builder()
                        .tabId(command.getTabId())
                        .items(command.getItems())
                        .build()
        );
    }

    @EventSourcingHandler
    void on(DrinksServed event){
        for (Integer menuItem : event.getItems()){
            OrderedItem orderedItem = outstandingDrinks.get(menuItem);
            if (orderedItem != null){
                outstandingDrinks.remove(menuItem);
                servedItemsValue = servedItemsValue.add(orderedItem.getPrice());
            }
        }
    }

    @CommandHandler
    public void markFoodPrepared(MarkFoodPrepared command){
        if (!isFoodOutstanding(command.getItems())){
            throw new FoodNotOutstandingException();
        }

        AggregateLifecycle.apply(
                FoodPrepared.builder()
                        .tabId(command.getTabId())
                        .items(command.getItems())
                        .build()
        );
    }

    @EventSourcingHandler
    void on(FoodPrepared event){
        for (Integer menuItem : event.getItems()){
            OrderedItem orderedItem = outstandingFood.get(menuItem);
            if (orderedItem != null){
                outstandingFood.remove(menuItem);
                preparedFood.put(menuItem, orderedItem);
            }
        }
    }

    @CommandHandler
    public void markFoodServed(MarkFoodServed command){
        if (!isFoodPrepared(command.getItems())){
            throw new FoodNotPreparedException();
        }

        AggregateLifecycle.apply(
                FoodServed.builder()
                        .tabId(command.getTabId())
                        .items(command.getItems())
                        .build()
        );
    }

    @EventSourcingHandler
    void on(FoodServed event){
        for (Integer menuItem : event.getItems()){
            OrderedItem orderedItem = preparedFood.get(menuItem);
            if (orderedItem != null){
                preparedFood.remove(menuItem);
                servedItemsValue = servedItemsValue.add(orderedItem.getPrice());
            }
        }
    }

    @CommandHandler
    public void closeTab(CloseTab command){

        if (!isTabOpened()){
            throw new TabNotOpenException();
        }

        if (hasUnservedItems()){
            throw new TabHasUnservedItemsException();
        }

        if (command.getAmountPaid().compareTo(servedItemsValue) < 0){
            throw new BillNotPaidException();
        }

        AggregateLifecycle.apply(
                TabClosed.builder()
                        .tabId(command.getTabId())
                        .amountPaid(command.getAmountPaid())
                        .orderPrice(servedItemsValue)
                        .tipValue(command.getAmountPaid().subtract(servedItemsValue))
                        .build()
        );
    }

    @EventSourcingHandler
    void on(TabClosed event){
        opened = false;
    }

  private boolean areDrinksOutstanding(Collection<Integer> menuNumbers){
    return outstandingDrinks.keySet().containsAll(menuNumbers);
  }

  private boolean isFoodOutstanding(Collection<Integer> menuNumbers){
    return outstandingFood.keySet().containsAll(menuNumbers);
  }

  private boolean isFoodPrepared(Collection<Integer> menuNumbers){
    return preparedFood.keySet().containsAll(menuNumbers);
  }

  public boolean hasUnservedItems(){
    return outstandingDrinks.size() > 0 || outstandingFood.size() > 0 || preparedFood.size() > 0;
  }

}
