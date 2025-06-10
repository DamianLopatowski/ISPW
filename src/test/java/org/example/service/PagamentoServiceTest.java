package org.example.service;

import org.example.dao.PagamentoDAO;
import org.example.model.Pagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PagamentoServiceTest {

    private PagamentoDAO pagamentoDAOMock;
    private PagamentoService pagamentoService;

    @BeforeEach
    public void setUp() {
        pagamentoDAOMock = mock(PagamentoDAO.class);
        pagamentoService = new PagamentoService(pagamentoDAOMock);
    }


    @Test
    public void testRegistraPagamento() {
        Pagamento pagamento = new Pagamento();
        pagamentoService.registraPagamento(pagamento);
        verify(pagamentoDAOMock, times(1)).registraPagamento(pagamento);
    }

    @Test
    public void testGetPagamentiPerCliente() {
        Pagamento pagamento1 = new Pagamento();
        Pagamento pagamento2 = new Pagamento();
        List<Pagamento> pagamenti = Arrays.asList(pagamento1, pagamento2);

        when(pagamentoDAOMock.getPagamentiPerCliente("cliente123")).thenReturn(pagamenti);

        List<Pagamento> result = pagamentoService.getPagamentiPerCliente("cliente123");

        assertEquals(2, result.size());
        assertSame(pagamento1, result.get(0));
        assertSame(pagamento2, result.get(1));
        verify(pagamentoDAOMock, times(1)).getPagamentiPerCliente("cliente123");
    }
}
