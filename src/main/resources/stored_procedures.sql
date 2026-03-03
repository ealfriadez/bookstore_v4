
-- Reporte: ventas por libro (solo compras COMPLETED)
-- Parametros opcionales: si se pasa NULL, no filtra por fecha
CREATE OR REPLACE FUNCTION fn_sales_report_by_book(
    p_date_from TIMESTAMP DEFAULT NULL,
    p_date_to TIMESTAMP DEFAULT NULL
)
RETURNS TABLE(
    book_id BIGINT,
    book_title VARCHAR,
    total_quantity BIGINT,
    total_revenue NUMERIC,
    total_purchases BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        b.id,
        b.title,
        SUM(pi.quantity),
        SUM(pi.subtotal),
        COUNT(DISTINCT p.id)
    FROM purchase_items pi
    JOIN books b ON b.id = pi.book_id
    JOIN purchases p ON p.id = pi.purchase_id
    WHERE p.status = 'COMPLETED'
      AND (p_date_from IS NULL OR p.purchase_date >= p_date_from)
      AND (p_date_to IS NULL OR p.purchase_date <= p_date_to)
    GROUP BY b.id, b.title
    ORDER BY SUM(pi.subtotal) DESC;
END;
$$ LANGUAGE plpgsql;

-- Reporte: resumen general de ventas (solo compras COMPLETED)
-- Parametros opcionales: si se pasa NULL, no filtra por fecha
CREATE OR REPLACE FUNCTION fn_sales_summary(
    p_date_from TIMESTAMP DEFAULT NULL,
    p_date_to TIMESTAMP DEFAULT NULL
)
RETURNS TABLE(
    total_sales NUMERIC,
    total_purchases BIGINT,
    average_per_purchase NUMERIC,
    best_seller_id BIGINT,
    best_seller_title VARCHAR,
    best_seller_quantity BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COALESCE(SUM(p.total), 0),
        COUNT(p.id),
        COALESCE(AVG(p.total), 0),
        bs.book_id,
        bs.title,
        bs.qty
    FROM purchases p
    LEFT JOIN (
        SELECT pi.book_id, b.title, SUM(pi.quantity) AS qty
        FROM purchase_items pi
        JOIN books b ON b.id = pi.book_id
        JOIN purchases p2 ON p2.id = pi.purchase_id
        WHERE p2.status = 'COMPLETED'
          AND (p_date_from IS NULL OR p2.purchase_date >= p_date_from)
          AND (p_date_to IS NULL OR p2.purchase_date <= p_date_to)
        GROUP BY pi.book_id, b.title
        ORDER BY SUM(pi.quantity) DESC
        LIMIT 1
    ) bs ON TRUE
    WHERE p.status = 'COMPLETED'
      AND (p_date_from IS NULL OR p.purchase_date >= p_date_from)
      AND (p_date_to IS NULL OR p.purchase_date <= p_date_to)
    GROUP BY bs.book_id, bs.title, bs.qty;
END;
$$ LANGUAGE plpgsql;
