package fun.cmgraph.service;

import fun.cmgraph.vo.OrderReportVO;
import fun.cmgraph.vo.SalesTop10ReportVO;
import fun.cmgraph.vo.TurnoverReportVO;
import fun.cmgraph.vo.UserReportVO;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO getTurnover(LocalDate begin, LocalDate end);

    UserReportVO getUser(LocalDate begin, LocalDate end);

    OrderReportVO getOrder(LocalDate begin, LocalDate end);

    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    void exportBusinessData(HttpServletResponse response);
}
