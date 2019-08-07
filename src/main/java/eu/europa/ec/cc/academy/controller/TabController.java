package eu.europa.ec.cc.academy.controller;

import eu.europa.ec.cc.academy.api.command.*;
import eu.europa.ec.cc.academy.eventhandler.TabReadModel;
import eu.europa.ec.cc.academy.queryhandler.FindTabsQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author <a href="thomas.frezel@ext.ec.europa.eu">Thomas Frezel</a>
 * @version $
 */
@RestController
@RequestMapping(value = "/main")
@CrossOrigin(allowedHeaders = "*", exposedHeaders = "Content-Range", allowCredentials = "true")
@Api("Operations used in the tab management")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("unchecked")
public class TabController {

  private final CommandGateway commandGateway;

  private final QueryGateway queryGateway;

  @PostMapping(path = "/tabs")
  @ApiOperation("Opens a new tab")
  public ResponseEntity<String> openTab(
      @RequestBody OpenTab command) {

    UUID result = commandGateway.sendAndWait(command);

    return new ResponseEntity<>(result.toString(), HttpStatus.OK);
  }

  @GetMapping(path = "/tabs")
  @ApiOperation("Returns list of tabs")
  public ResponseEntity<Page<TabReadModel>> openTab(
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam(value = "tableNumber", required = false) Optional<Integer> tableNumber)
      throws ExecutionException, InterruptedException {

    FindTabsQuery criteria = new FindTabsQuery();
    criteria.setPageable(PageRequest.of(page, size));
    
    tableNumber.ifPresent(criteria::setTableNumber);

    CompletableFuture<Page> result = queryGateway.query(criteria, Page.class);
    return new ResponseEntity<>(result.get(), HttpStatus.OK);
  }

  @PutMapping(path = "/tabs/orders")
  @ApiOperation("Order food or drinks")
  public ResponseEntity<?> placeOrder(
      @RequestBody PlaceOrder command)
      throws ExecutionException, InterruptedException {

    CompletableFuture<?> result = commandGateway.send(command);
    return new ResponseEntity<>(result.get(), HttpStatus.OK);

  }

  @PutMapping(path = "/tabs/drinks/served")
  @ApiOperation("Mark drinks as served")
  public ResponseEntity<?> serveDrinks(
      @RequestBody MarkDrinkServed command)
      throws ExecutionException, InterruptedException {

    CompletableFuture<?> result = commandGateway.send(command);
    return new ResponseEntity<>(result.get(), HttpStatus.OK);

  }

  @PutMapping(path = "/tabs/food/prepared")
  @ApiOperation("Mark food as prepared")
  public ResponseEntity<?> prepareFood(
      @RequestBody MarkFoodPrepared command)
      throws ExecutionException, InterruptedException {

    CompletableFuture<?> result = commandGateway.send(command);
    return new ResponseEntity<>(result.get(), HttpStatus.OK);

  }

  @PutMapping(path = "/tabs/food/served")
  @ApiOperation("Mark food as served")
  public ResponseEntity<?> prepareFood(
      @RequestBody MarkFoodServed command)
      throws ExecutionException, InterruptedException {

    CompletableFuture<?> result = commandGateway.send(command);
    return new ResponseEntity<>(result.get(), HttpStatus.OK);

  }

  @DeleteMapping(path = "/tabs")
  @ApiOperation("Close tab")
  public ResponseEntity<?> closeTab(
      @RequestBody CloseTab command)
      throws ExecutionException, InterruptedException {

    CompletableFuture<?> result = commandGateway.send(command);
    return new ResponseEntity<>(result.get(), HttpStatus.OK);

  }

}
