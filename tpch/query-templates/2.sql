-- $ID$
-- TPC-H/TPC-R Minimum Cost Supplier Query (Q2)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	s_acctbal,
	s_name,
	n_name,
	p_partkey,
	p_mfgr,
	s_address,
	s_phone,
	s_comment
from
	tpch2.tpch.part,
	tpch1.tpch.supplier,
	tpch2.tpch.partsupp,
	tpch1.tpch.nation,
	tpch1.tpch.region
where
	p_partkey = ps_partkey
	and s_supplierkey = ps_supplierkey
	and p_size = :1
	and p_type like '%:2'
	and s_nationkey = n_nationkey
	and n_regionkey = r_regionkey
	and r_name = ':3'
	and ps_supplycost = (
		select
			min(ps_supplycost)
		from
			tpch2.tpch.partsupp,
			tpch1.tpch.supplier,
			tpch1.tpch.nation,
			tpch1.tpch.region
		where
			p_partkey = ps_partkey
			and s_supplierkey = ps_supplierkey
			and s_nationkey = n_nationkey
			and n_regionkey = r_regionkey
			and r_name = ':3'
	)
order by
	s_acctbal desc,
	n_name,
	s_name,
	p_partkey;
