# HỆ THỐNG TÀI LIỆU THIẾT KẾ DỰ ÁN LAPBOT
## Website Bán Laptop React Tích Hợp Chat-to-Order & Machine Learning API

---

## 📌 GIỚI THIỆU TỔNG QUAN
Thư mục **`LapBot`** chứa toàn bộ hệ thống tài liệu thiết kế nghiệp vụ, trải nghiệm giao diện Generative UI, kiến trúc kỹ thuật Frontend & Admin, hợp đồng API và bộ kịch bản Prompt cho dự án **LapBot** - Giải pháp chuyển đổi hội thoại tư vấn laptop thành đơn hàng trực tiếp (**Chat-to-Order**).

Toàn bộ tài liệu được chuẩn hóa dưới định dạng **Markdown chuyên sâu**, tích hợp các sơ đồ **Mermaid** (Flowchart, Sequence, State Machine, ERD), không chứa file mã nguồn thực thi theo đúng yêu cầu thiết kế.

---

## 🗂️ MỤC LỤC BỘ TÀI LIỆU

| Thứ tự | Tên File | Vai trò & Nội dung cốt lõi | Liên kết nhanh |
| :---: | :--- | :--- | :---: |
| **00** | `00_TONG_QUAN_KIEN_TRUC_HE_THONG.md` | **Tổng quan kiến trúc toàn hệ thống**: Mô hình phân lớp Client - Gateway - Core Backend - AI Orchestrator - CSDL, sơ đồ tương tác E2E, ma trận phân quyền. | [Xem tài liệu](file:///d:/Coder/AI_Train/TH04/LapBot/00_TONG_QUAN_KIEN_TRUC_HE_THONG.md) |
| **01** | `01_TAI_LIEU_NGHIEP_VU.md` | **Tài liệu nghiệp vụ (BRD / SRS)**: Quy trình Chat-to-Order, phân loại khách hàng (Guest vs Registered), kiểm tra tồn kho & khóa tạm 15 phút (Soft lock), State Machine vòng đời đơn hàng. | [Xem tài liệu](file:///d:/Coder/AI_Train/TH04/LapBot/01_TAI_LIEU_NGHIEP_VU.md) |
| **02** | `02_TAI_LIEU_GIAO_DIEN_AI_THUC_THI.md` | **Đặc tả giao diện AI thực thi (Generative UI)**: Thiết kế wireframe các widget tương tác trong chat: `LaptopSpecCard`, `VariantCustomizerWidget`, `OrderPreviewCard`, `VietQRCard`, `InlineAddressFormWidget`. | [Xem tài liệu](file:///d:/Coder/AI_Train/TH04/LapBot/02_TAI_LIEU_GIAO_DIEN_AI_THUC_THI.md) |
| **03** | `03_TAI_LIEU_KY_THUAT_AI_FRONTEND.md` | **Kỹ thuật AI phía Frontend (React)**: Quản lý trạng thái bằng Zustand, bộ giải mã Server-Sent Events (SSE Stream Parser), Component Registry động, Event Bus đồng bộ giỏ hàng với Web chính. | [Xem tài liệu](file:///d:/Coder/AI_Train/TH04/LapBot/03_TAI_LIEU_KY_THUAT_AI_FRONTEND.md) |
| **04** | `04_TAI_LIEU_KY_THUAT_AI_ADMIN.md` | **Kỹ thuật AI phía Admin**: Bàn điều khiển Human-in-the-loop (HITL Takeover), giám sát phiên chat realtime qua WebSocket, pipeline RAG Ingestion tri thức sản phẩm, phân tích phễu chuyển đổi. | [Xem tài liệu](file:///d:/Coder/AI_Train/TH04/LapBot/04_TAI_LIEU_KY_THUAT_AI_ADMIN.md) |
| **05** | `05_THIET_KE_API_ML_VA_CSDL.md` | **Đặc tả API & Cơ sở dữ liệu**: Chuẩn hóa REST/SSE Endpoints, Schemas công cụ Function Calling cho LLM, thiết kế CSDL quan hệ (ERD Mermaid) với đầy đủ từ điển dữ liệu. | [Xem tài liệu](file:///d:/Coder/AI_Train/TH04/LapBot/05_THIET_KE_API_ML_VA_CSDL.md) |
| **06** | `06_KICH_BAN_CHAT_TO_ORDER_VA_PROMPT_SYSTEM.md` | **Kịch bản hội thoại & Prompt Master**: System Prompt chi tiết, cơ chế phòng vệ chống Jailbreak/Ảo giác giá, 5 kịch bản hội thoại đa lượt thực tế (Happy path, nâng cấp RAM, xử lý hết hàng, bắt lỗi SĐT, can thiệp HITL). | [Xem tài liệu](file:///d:/Coder/AI_Train/TH04/LapBot/06_KICH_BAN_CHAT_TO_ORDER_VA_PROMPT_SYSTEM.md) |

---

## 🎯 CÁC TÍNH NĂNG NỔI BẬT ĐÃ ĐƯỢC THIẾT KẾ
1. **Generative UI**: AI không chỉ nhắn tin đơn thuần mà xuất trực tiếp các thẻ mua hàng, bộ chọn cấu hình và mã thanh toán QR động ngay trong dòng chat.
2. **Khóa tồn kho an toàn (Soft Lock TTL)**: Giữ hàng 15 phút tránh hiện tượng bán vượt tồn kho (overselling) khi khách đang xem lại đơn hàng.
3. **Thanh toán VietQR tức thời**: Tự động sinh mã VietQR Napas247 chính xác số tiền và mã đơn hàng, tự động xác nhận khi tiền vào tài khoản.
4. **Hỗ trợ song song Guest & Member**: Khách không bắt buộc đăng nhập vẫn có thể đặt mua nhanh chỉ với Tên, SĐT và Địa chỉ.
5. **Cơ chế Human-in-the-Loop (HITL)**: Nhân viên CSKH có thể bấm tiếp quản cuộc trò chuyện bất kỳ lúc nào khi AI gặp tình huống phức tạp.
