package eu.europa.ec.cc.academy.eventhandler;

import com.google.common.collect.Lists;
import eu.europa.ec.cc.academy.api.event.*;
import eu.europa.ec.cc.academy.domain.OrderedItemVO;
import eu.europa.ec.cc.academy.infrastructure.TabRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@ProcessingGroup("tab-read")
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TabEventHandler {

    private final TabRepository repository;

    @EventHandler
    public void on(TabOpened event){
        TabReadModel readModel = new TabReadModel();
        readModel.setTabId(event.getTabId().toString());
        readModel.setWaiter(event.getWaiter());
        readModel.setTableNumber(event.getTableNumber());
        readModel.setOpened(true);
        repository.save(readModel);
    }

    @EventHandler
    public void on(DrinksOrdered event){
        Optional<TabReadModel> byId = repository.findById(event.getTabId().toString());
        if (byId.isPresent()){
            TabReadModel readModel = byId.get();

            for (OrderedItemVO item : event.getItems()){
                TabOrderedItemReadModel oir = new TabOrderedItemReadModel();
                oir.setItem(item.getItem());
                oir.setDescription(item.getDescription());
                oir.setDrink(item.isDrink());
                oir.setPrice(item.getPrice().floatValue());

                if (readModel.getOutstanding() == null){
                    readModel.setOutstanding(Lists.newArrayList());
                }
                readModel.getOutstanding().add(oir);
            }

            repository.save(readModel);
        }
    }

    @EventHandler
    public void on(FoodOrdered event){
        Optional<TabReadModel> byId = repository.findById(event.getTabId().toString());
        if (byId.isPresent()){
            TabReadModel readModel = byId.get();

            for (OrderedItemVO item : event.getItems()){
                TabOrderedItemReadModel oir = new TabOrderedItemReadModel();
                oir.setItem(item.getItem());
                oir.setDescription(item.getDescription());
                oir.setDrink(item.isDrink());
                oir.setPrice(item.getPrice().floatValue());

                if (readModel.getOutstanding() == null){
                    readModel.setOutstanding(Lists.newArrayList());
                }
                readModel.getOutstanding().add(oir);
            }

            repository.save(readModel);
        }
    }


}
