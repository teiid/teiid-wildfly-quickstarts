CREATE TABLE tpch.region(
   r_regionkey integer,
   r_name VARCHAR(25),
   r_comment VARCHAR(152),
   CONSTRAINT pk_r_regionkey PRIMARY KEY ( r_regionkey)
);
CREATE TABLE tpch.nation(
   n_nationkey integer,
   n_name VARCHAR(25),
   n_regionkey integer,
   n_comment VARCHAR(152),
   CONSTRAINT pk_n_nationkey PRIMARY KEY ( n_nationkey),
   CONSTRAINT fk_r_regionkey FOREIGN KEY (n_regionkey) REFERENCES tpch.region (r_regionkey)
);
CREATE TABLE tpch.customer(
   c_customerkey integer,
   c_name VARCHAR(25),
   c_address VARCHAR(40),
   c_nationkey integer,
   c_phone VARCHAR(15),
   c_acctbal Decimal,
   c_mktsegment VARCHAR(10),
   c_comment VARCHAR(117),
   CONSTRAINT pk_c_customerkey PRIMARY KEY ( c_customerkey),
   CONSTRAINT fk_n_nationkey FOREIGN KEY (c_nationkey) REFERENCES tpch.nation (n_nationkey)
);
CREATE TABLE tpch.supplier(
   s_supplierkey integer,
   s_name VARCHAR(25),
   s_address VARCHAR(40),
   s_nationkey integer,
   s_phone VARCHAR(15),
   s_acctbal Decimal,
   s_comment VARCHAR(101),
   CONSTRAINT pk_s_supplierkey PRIMARY KEY ( s_supplierkey),
   CONSTRAINT fk_n_nationkey FOREIGN KEY (s_nationkey) REFERENCES tpch.nation (n_nationkey)
);
CREATE TABLE tpch.part(
   p_partkey integer,
   p_name VARCHAR(55),
   p_mfgr VARCHAR(15),
   p_brand VARCHAR(10),
   p_type VARCHAR(25),
   p_size integer,
   p_container VARCHAR(10),
   p_retailprice Decimal,
   p_comment VARCHAR(23),
   CONSTRAINT pk_p_partkey PRIMARY KEY ( p_partkey)
);
CREATE TABLE tpch.partsupp(
   ps_partkey integer,
   ps_supplierkey integer,
   ps_availqty integer,
   ps_supplycost Decimal,
   ps_comment VARCHAR(199),
   CONSTRAINT pk_ps_partkey PRIMARY KEY ( ps_partkey, ps_supplierkey),
   CONSTRAINT fk_p_partkey FOREIGN KEY (ps_partkey) REFERENCES tpch.part (p_partkey),
   CONSTRAINT fk_s_supplierkey FOREIGN KEY (ps_supplierkey) REFERENCES tpch.supplier (s_supplierkey)
);
CREATE TABLE tpch.orders(
   o_orderskey integer,
   o_customerkey integer,
   o_orderstatus VARCHAR(1),
   o_totalprice Decimal,
   o_orderdate DATE,
   o_orderpriority VARCHAR(15),
   o_clerk VARCHAR(15),
   o_shippriority integer,
   o_comment VARCHAR(79),
   CONSTRAINT pk_o_orderskey PRIMARY KEY ( o_orderskey),
   CONSTRAINT fk_c_customerkey FOREIGN KEY (o_customerkey) REFERENCES tpch.customer (c_customerkey)
 );
CREATE TABLE tpch.lineitem(
   l_orderskey integer,
   l_partkey integer,
   l_supplierkey integer,
   l_linenumber integer,
   l_quantity Decimal,
   l_extendedprice Decimal,
   l_discount Decimal,
   l_tax Decimal,
   l_returnflag VARCHAR(1),
   l_linestatus VARCHAR(1),
   l_shipdate DATE,
   l_commitdate DATE,
   l_receiptdate DATE,
   l_shipinstruct VARCHAR(25),
   l_shipmode VARCHAR(10),
   l_comment VARCHAR(44),
   CONSTRAINT pk_l_orderskey PRIMARY KEY ( l_orderskey, l_linenumber),
   CONSTRAINT fk_o_orderskey FOREIGN KEY (l_orderskey) REFERENCES tpch.orders (o_orderskey),
   CONSTRAINT fk_ps_partkey_ps_supplierkey FOREIGN KEY (l_partkey ,l_supplierkey ) REFERENCES tpch.partsupp (ps_partkey,ps_supplierkey)
);
