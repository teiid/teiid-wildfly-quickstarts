CREATE INDEX nation_n_regionkey_idx ON tpch.nation (n_regionkey);
CREATE INDEX customer_c_nationkey_idx ON tpch.customer (c_nationkey);
CREATE INDEX supplier_s_nationkey_idx ON tpch.supplier (s_nationkey);
CREATE INDEX partsupp_ps_partkey_idx ON tpch.partsupp (ps_partkey);
CREATE INDEX partsupp_ps_supplierkey_idx ON tpch.partsupp (ps_supplierkey);
CREATE INDEX orders_o_customerkey_idx ON tpch.orders (o_customerkey);
CREATE INDEX lineitem_l_partkey_idx ON tpch.lineitem (l_partkey);
CREATE INDEX lineitem_l_orderskey_idx ON tpch.lineitem (l_orderskey);
CREATE INDEX lineitem_l_supplierkey_idx ON tpch.lineitem (l_supplierkey);

