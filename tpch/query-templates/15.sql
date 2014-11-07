-- $ID$
-- TPC-H/TPC-R Top Supplier Query (Q15)
-- Functional Query Definition
-- Approved February 1998
:x
:o

select
	s_supplierkey,
	s_name,
	s_address,
	s_phone,
	total_revenue
from
	tpch1.tpch.supplier,
	(	select
		l_supplierkey as supplier_no,
		sum(l_extendedprice * (1 - l_discount)) as total_revenue
	from
		tpch2.tpch.lineitem
	where
		l_shipdate >=  ':1'
		and l_shipdate <  TIMESTAMPADD(SQL_TSI_MONTH,'3', ':1')
	group by
		l_supplierkey) as revenue
where
	s_supplierkey = supplier_no
	and total_revenue = (
		select
			max(revWhere.total_revenue)
		from
			(	select
		l_supplierkey as supplier_no,
		sum(l_extendedprice * (1 - l_discount)) as total_revenue
	from
		tpch2.tpch.lineitem
	where
		l_shipdate >=  ':1'
		and l_shipdate <  TIMESTAMPADD(SQL_TSI_MONTH,'3', ':1')
	group by
		l_supplierkey) as revWhere
	)
order by
	s_supplierkey;


