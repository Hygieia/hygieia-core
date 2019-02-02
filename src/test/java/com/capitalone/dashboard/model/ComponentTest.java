package com.capitalone.dashboard.model;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ComponentTest {

    @Test
    public void testGetLastUpdatedCollectorItemForType() {
        Component component = new Component();
        List<CollectorItem> cItems = new ArrayList<>();
        cItems.add(makeCollectorItem(1537476665987L));
        cItems.add(makeCollectorItem(0));
        cItems.add(makeCollectorItem(1537471111111L));
        cItems.add(makeCollectorItem(1537472222222L));
        cItems.add(makeCollectorItem(1537473333333L));
        component.getCollectorItems().put(CollectorType.SCM, cItems);
        CollectorItem c = component.getLastUpdatedCollectorItemForType(CollectorType.SCM);
        Assert.assertArrayEquals(new long[]{1537476665987L}, new long[]{c.getLastUpdated()});

    }

    @Test
    public void testGetLastUpdatedCollectorItemForTypeForOneItem() {
        Component component = new Component();
        List<CollectorItem> cItems = new ArrayList<>();
        cItems.add(makeCollectorItem(1537473333333L));
        component.getCollectorItems().put(CollectorType.SCM, cItems);
        CollectorItem c = component.getLastUpdatedCollectorItemForType(CollectorType.SCM);
        Assert.assertArrayEquals(new long[]{1537473333333L}, new long[]{c.getLastUpdated()});

    }

    @Test
    public void testGetLastUpdatedCollectorItemForTypeForZero() {
        Component component = new Component();
        List<CollectorItem> cItems = new ArrayList<>();
        cItems.add(makeCollectorItem(1537473333333L));
        cItems.add(makeCollectorItem(0));
        component.getCollectorItems().put(CollectorType.SCM, cItems);
        CollectorItem c = component.getLastUpdatedCollectorItemForType(CollectorType.SCM);
        Assert.assertArrayEquals(new long[]{1537473333333L}, new long[]{c.getLastUpdated()});

    }

    @Test
    public void testGetLastUpdatedCollectorItemForTypeForZeros() {
        Component component = new Component();
        List<CollectorItem> cItems = new ArrayList<>();
        cItems.add(makeCollectorItem(0));
        cItems.add(makeCollectorItem(0));
        component.getCollectorItems().put(CollectorType.SCM, cItems);
        CollectorItem c = component.getLastUpdatedCollectorItemForType(CollectorType.SCM);
        Assert.assertArrayEquals(new long[]{0}, new long[]{c.getLastUpdated()});

    }

    @Test
    public void testGetLastUpdatedCollectorItemForTypeForOneItemZero() {
        Component component = new Component();
        List<CollectorItem> cItems = new ArrayList<>();
        cItems.add(makeCollectorItem(0));
        component.getCollectorItems().put(CollectorType.SCM, cItems);
        CollectorItem c = component.getLastUpdatedCollectorItemForType(CollectorType.SCM);
        Assert.assertArrayEquals(new long[]{0}, new long[]{c.getLastUpdated()});

    }

    @Test
    public void testUpdateCollectorItem() {
        Component component = createComponent(1537473333333L);
        CollectorItem c = makeCollectorItem(1537476665987L);
        c.setId(component.getCollectorItems(CollectorType.SCM).get(0).getId());
        component.updateCollectorItem(CollectorType.SCM, c);
        Assert.assertEquals(component.getCollectorItems(CollectorType.SCM).get(0).getLastUpdated(), 1537476665987L);
    }

    @Test
    public void testUpdateCollectorItem_NoMatch() {
        Component component = createComponent(1537473333333L);
        CollectorItem c = makeCollectorItem(1537476665987L);
        component.updateCollectorItem(CollectorType.SCM, c);
        Assert.assertEquals(component.getCollectorItems(CollectorType.SCM).get(0).getLastUpdated(), 1537473333333L);
    }

    @Test
    public void testFindByCollectorItemAndType() {
        Component component = createComponent(1537473333333L);
        CollectorItem c1 = new CollectorItem();
        ObjectId collectorItemId = ObjectId.get();
        c1.setId(collectorItemId);
        c1.setLastUpdated(1537476665987L);
        component.addCollectorItem(CollectorType.SCM, c1);

        CollectorItem result = component.getCollectorItemMatchingTypeAndCollectorItemId(CollectorType.SCM, collectorItemId);

        Assert.assertEquals(result.getId(), collectorItemId);
    }

    @Test
    public void testFindByCollectorItemAndTypeNotFound() {
        Component component = createComponent(1537473333333L);
        CollectorItem c1 = new CollectorItem();
        ObjectId collectorItemId = ObjectId.get();
        c1.setId(collectorItemId);
        c1.setLastUpdated(1537476665987L);
        component.addCollectorItem(CollectorType.SCM, c1);

        CollectorItem result = component.getCollectorItemMatchingTypeAndCollectorItemId(CollectorType.SCM, ObjectId.get());

        Assert.assertNull(result);
    }

    @Test
    public void testFindByCollectorItemAndTypeNoType() {
        Component component = createComponent(1537473333333L);
        CollectorItem c1 = new CollectorItem();
        ObjectId collectorItemId = ObjectId.get();
        c1.setId(collectorItemId);
        c1.setLastUpdated(1537476665987L);
        component.addCollectorItem(CollectorType.SCM, c1);

        CollectorItem result = component.getCollectorItemMatchingTypeAndCollectorItemId(CollectorType.Build, collectorItemId);

        Assert.assertNull(result);
    }

    @Test
    public void testDuplicateCollectorItemsAllowed() {
        Component component = createComponent(1537473333333L);
        CollectorItem c1 = new CollectorItem();
        ObjectId collectorItemId = ObjectId.get();
        c1.setId(collectorItemId);
        c1.setLastUpdated(1537476665987L);

        //pre-check
        List<CollectorItem> collectorItems = component.getCollectorItems(CollectorType.SCM);
        Assert.assertEquals(1, collectorItems.size());

        // add in duplicates
        component.addCollectorItem(CollectorType.SCM, c1);
        component.addCollectorItem(CollectorType.SCM, c1);

        // make sure we have 2more
        List<CollectorItem> collectorItemsAfter = component.getCollectorItems(CollectorType.SCM);
        Assert.assertEquals(3, collectorItemsAfter.size());
    }


    private CollectorItem makeCollectorItem(long lastUpdated) {
        CollectorItem c = new CollectorItem();
        c.setId(ObjectId.get());
        c.setLastUpdated(lastUpdated);
        return c;
    }

    private Component createComponent(long lastUpdated) {
        Component component = new Component();
        component.getCollectorItems().put(CollectorType.SCM, new ArrayList(Collections.singleton(makeCollectorItem(lastUpdated))));
        return component;
    }
}
