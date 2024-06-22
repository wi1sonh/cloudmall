package fun.cmgraph.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fun.cmgraph.dto.*;
import fun.cmgraph.utils.HttpClientUtil;
import fun.cmgraph.entity.Bundle;
import fun.cmgraph.mapper.BundleMapper;
import fun.cmgraph.service.serviceImpl.BundleServiceImpl;

@ExtendWith(MockitoExtension.class)
class BundleServiceTest {

    @Mock
    private BundleMapper bundleMapper;

    @InjectMocks
    private BundleServiceImpl bundleService;

    @Test
    void addBundle() {
    }

    @Test
    void getPageList() {
    }

    @Test
    void getBundleById() {
    }

    @Test
    void onOff() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteBatch() {
    }

    @Test
    void getList() {
    }

    @Test
    void getBundleProductById() {
    }

    @Test
    void getBundleWithPic() {
    }
}