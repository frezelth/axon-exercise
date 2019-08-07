package eu.europa.ec.cc.academy.domain;

import eu.europa.ec.cc.academy.api.command.*;
import eu.europa.ec.cc.academy.eventhandler.TabReadModel;
import eu.europa.ec.cc.academy.infrastructure.TabRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"integrationtests"})
public class TestServiceIntegrationTest {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private TabRepository repository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Before
    public void setUp() throws Exception {
        elasticsearchTemplate.deleteIndex(TabReadModel.class);
        elasticsearchTemplate.createIndex(TabReadModel.class);
        elasticsearchTemplate.putMapping(TabReadModel.class);
        elasticsearchTemplate.refresh(TabReadModel.class);
    }

    @Test
    public void testFullScenario() {

        OpenTab tab = OpenTab.builder()
                .tableNumber(1)
                .waiter("Bruce Willis")
                .build();

        UUID id = commandGateway.sendAndWait(tab);

        Optional<TabReadModel> byId = repository.findById(id.toString());
        if (!byId.isPresent()) {
            Assert.fail("Item not found");
        }

        Assert.assertEquals(id.toString(), byId.get().getTabId());
        Assert.assertEquals((Integer) 1, byId.get().getTableNumber());
        Assert.assertEquals("Bruce Willis", byId.get().getWaiter());
        Assert.assertEquals(true, byId.get().getOpened());

        PlaceOrder orderCommand = PlaceOrder.builder()
                .tabId(id)
                .item(OrderedItemVO.builder()
                        .item(1)
                        .drink(true)
                        .description("Big beer")
                        .price(new BigDecimal("5"))
                        .build())
                .item(OrderedItemVO.builder()
                        .item(10)
                        .drink(false)
                        .description("Big burger")
                        .price(new BigDecimal("20"))
                        .build())
                .build();

        commandGateway.sendAndWait(orderCommand);

        byId = repository.findById(id.toString());
        if (!byId.isPresent()) {
            Assert.fail("Item not found");
        }

        Assert.assertEquals(id.toString(), byId.get().getTabId());
        Assert.assertEquals(2, byId.get().getOutstanding().size());


        MarkDrinkServed markDrinkServedCommand = MarkDrinkServed.builder()
                .tabId(id)
                .item(1)
                .build();

        commandGateway.sendAndWait(markDrinkServedCommand);

        byId = repository.findById(id.toString());
        if (!byId.isPresent()) {
            Assert.fail("Item not found");
        }

        Assert.assertEquals(id.toString(), byId.get().getTabId());
        Assert.assertEquals(1, byId.get().getOutstanding().size());
        Assert.assertEquals(1, byId.get().getServed().size());

        MarkFoodPrepared markFoodPrepared = MarkFoodPrepared.builder()
                .tabId(id)
                .item(10)
                .build();

        commandGateway.sendAndWait(markFoodPrepared);

        byId = repository.findById(id.toString());
        if (!byId.isPresent()) {
            Assert.fail("Item not found");
        }

        Assert.assertEquals(id.toString(), byId.get().getTabId());
        Assert.assertEquals(0, byId.get().getOutstanding().size());
        Assert.assertEquals(1, byId.get().getServed().size());
        Assert.assertEquals(1, byId.get().getPrepared().size());

        MarkFoodServed markFoodServed = MarkFoodServed.builder()
                .tabId(id)
                .item(10)
                .build();

        commandGateway.sendAndWait(markFoodServed);

        byId = repository.findById(id.toString());
        if (!byId.isPresent()) {
            Assert.fail("Item not found");
        }

        Assert.assertEquals(id.toString(), byId.get().getTabId());
        Assert.assertEquals(0, byId.get().getOutstanding().size());
        Assert.assertEquals(2, byId.get().getServed().size());
        Assert.assertEquals(0, byId.get().getPrepared().size());

        CloseTab closeTab = CloseTab.builder()
                .tabId(id)
                .amountPaid(new BigDecimal("30"))
                .build();

        commandGateway.sendAndWait(closeTab);

        byId = repository.findById(id.toString());
        if (!byId.isPresent()) {
            Assert.fail("Item not found");
        }

        Assert.assertEquals(id.toString(), byId.get().getTabId());
        Assert.assertEquals(0, byId.get().getOutstanding().size());
        Assert.assertEquals(2, byId.get().getServed().size());
        Assert.assertEquals(0, byId.get().getPrepared().size());

        Assert.assertEquals((Float) 30f, byId.get().getAmountPaid());
        Assert.assertEquals((Float)25f, byId.get().getOrderPrice());
        Assert.assertEquals((Float)5f, byId.get().getTipValue());
    }
}
