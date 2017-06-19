package com.bi.oranj.service;

import com.bi.oranj.repository.bi.FirmRepository;
import com.bi.oranj.service.bi.FirmService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * Created by jaloliddinbakirov on 6/19/17.
 */
public class FirmServiceTest {

    private FirmService firmService;
    private FirmRepository firmRepositoryMock;

    @Before
    public void setUp(){
        firmRepositoryMock = Mockito.mock(FirmRepository.class);
        firmService = new FirmService();
    }

    @Test
    public void findDistinctFromFirm() throws Exception{
        when(firmRepositoryMock.findDistinctFromFirm()).thenReturn(1);

    }

}
