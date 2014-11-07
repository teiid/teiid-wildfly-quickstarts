-- $ID$
-- TPC-H/TPC-R Global Sales Opportunity Query (Q22)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	cntrycode,
	count(*) as numcust,
	sum(c_acctbal) as totacctbal
from
	(
		select
			substring(c_phone from 1 for 2) as cntrycode,
			c_acctbal
		from
			tpch1.tpch.customer
		where
			substring(c_phone from 1 for 2) in
				(':1', ':2', ':3', ':4', ':5', ':6', ':7')
			and c_acctbal > (
				select
					avg(c_acctbal)
				from
					tpch1.tpch.customer
				where
					c_acctbal > 0.00
					and substring(c_phone from 1 for 2) in
						(':1', ':2', ':3', ':4', ':5', ':6', ':7')
			)
			and not exists (
				select
					*
				from
					tpch2.tpch.orders
				where
					o_customerkey = c_customerkey
			)
	) as custsale
group by
	cntrycode
order by
	cntrycode;
