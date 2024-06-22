package fun.cmgraph.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fun.cmgraph.constant.StatusConstant;
import fun.cmgraph.dto.*;
import fun.cmgraph.entity.Bundle;
import fun.cmgraph.mapper.BundleMapper;
import fun.cmgraph.service.serviceImpl.BundleServiceImpl;
import fun.cmgraph.entity.BundleProduct;
import fun.cmgraph.mapper.BundleProductMapper;
import fun.cmgraph.vo.BundleVO;

import java.math.BigDecimal;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class BundleServiceTest {

    @Mock
    private BundleMapper bundleMapper;
    @Mock
    private BundleProductMapper bundleProductMapper;

    @InjectMocks
    private BundleServiceImpl bundleService;

    @Test
    void addBundle() {
        BundleDTO bundleDTO = new BundleDTO(1, "name", "pic", "detail", new BigDecimal(10), "status", 1, null);
        List<BundleProduct> bundleProducts = new ArrayList<>();
        bundleProducts.add(new BundleProduct(1, "name", new BigDecimal(6), 1, 1, 1));
        bundleProducts.add(new BundleProduct(2, "name", new BigDecimal(4), 1, 1, 2));
        bundleDTO.setBundleProducts(bundleProducts);
        bundleService.addBundle(bundleDTO);
        verify(bundleMapper, times(1)).addBundle(any(Bundle.class));
        verify(bundleProductMapper, times(1)).insertBatch(anyList());
    }

    @Test
    void getBundleById() {
        Bundle bundle = new Bundle(1, "name", "pic", "detail", new BigDecimal(10), 1, 1, 1, 1, null, null);
        when(bundleMapper.getBundleById(1)).thenReturn(bundle);
        BundleVO result = bundleService.getBundleById(1);
        assertEquals(1, result.getId());
    }

    @Test
    void onOff() {
        bundleService.onOff(1);
        verify(bundleMapper, times(1)).onOff(1);
    }

    @Test
    void update() {
        BundleDTO bundleDTO = new BundleDTO(1, "name", "pic", "detail", new BigDecimal(10), "status", 1, null);
        List<BundleProduct> bundleProducts = new ArrayList<>();
        bundleProducts.add(new BundleProduct(1, "name", new BigDecimal(6), 1, 1, 1));
        bundleProducts.add(new BundleProduct(2, "name", new BigDecimal(4), 1, 1, 2));
        bundleDTO.setBundleProducts(bundleProducts);
        bundleService.update(bundleDTO);
        verify(bundleMapper, times(1)).update(any(Bundle.class));
        verify(bundleProductMapper, times(1)).deleteBySetmealId(1);
        verify(bundleProductMapper, times(1)).insertBatch(anyList());
    }

    @Test
    void deleteBatch() {
        when(bundleMapper.getBundleById(1)).thenReturn(new Bundle());
        when(bundleMapper.getBundleById(2)).thenReturn(new Bundle());
        bundleService.deleteBatch(Arrays.asList(1, 2));
        verify(bundleMapper, times(2)).getBundleById(any());
        verify(bundleMapper, times(1)).deleteBatch(Arrays.asList(1, 2));
        verify(bundleProductMapper, times(1)).deleteBatch(Arrays.asList(1, 2));
    }

    @Test
    void getList() {
        Bundle bundle = new Bundle();
        bundle.setCategoryId(1);
        bundle.setStatus(StatusConstant.ENABLE);
        List<Bundle> bundles = new ArrayList<>();
        bundles.add(bundle);
        when(bundleMapper.getList(bundle)).thenReturn(bundles);
        List<Bundle> result = bundleService.getList(1);
        assertEquals(1, result.size());
    }

}