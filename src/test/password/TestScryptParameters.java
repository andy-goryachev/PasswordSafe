// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package test.password;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.CKit;
import goryachev.common.util.D;
import goryachev.common.util.SW;
import org.bouncycastle.crypto.generators.SCrypt;


public class TestScryptParameters
{
	public static void main(String[] args)
	{
		TF.run();
	}


//	@Test
	public void test() throws Exception
	{
		long maxmem = 10000000L;
		double maxmemfrac = 0.5;
		double maxtime = 1.0; // what's the unit?
		pickparams(maxmem, maxmemfrac, maxtime);
	}
	
	
//	static int
//	pickparams(size_t maxmem, double maxmemfrac, double maxtime,
//	    int * logN, uint32_t * r, uint32_t * p)
//	{
//		size_t memlimit;
//		double opps;
//		double opslimit;
//		double maxN, maxrp;
//		int rc;
//
//		/* Figure out how much memory to use. */
//		if (memtouse(maxmem, maxmemfrac, &memlimit))
//			return (1);
//
//		/* Figure out how fast the CPU is. */
//		if ((rc = scryptenc_cpuperf(&opps)) != 0)
//			return (rc);
//		opslimit = opps * maxtime;
//
//		/* Allow a minimum of 2^15 salsa20/8 cores. */
//		if (opslimit < 32768)
//			opslimit = 32768;
//
//		/* Fix r = 8 for now. */
//		*r = 8;
//
//		/*
//		 * The memory limit requires that 128Nr <= memlimit, while the CPU
//		 * limit requires that 4Nrp <= opslimit.  If opslimit < memlimit/32,
//		 * opslimit imposes the stronger limit on N.
//		 */
//	#ifdef DEBUG
//		fprintf(stderr, "Requiring 128Nr <= %zu, 4Nrp <= %f\n",
//		    memlimit, opslimit);
//	#endif
//		if (opslimit < memlimit/32) {
//			/* Set p = 1 and choose N based on the CPU limit. */
//			*p = 1;
//			maxN = opslimit / (*r * 4);
//			for (*logN = 1; *logN < 63; *logN += 1) {
//				if ((uint64_t)(1) << *logN > maxN / 2)
//					break;
//			}
//		} else {
//			/* Set N based on the memory limit. */
//			maxN = memlimit / (*r * 128);
//			for (*logN = 1; *logN < 63; *logN += 1) {
//				if ((uint64_t)(1) << *logN > maxN / 2)
//					break;
//			}
//
//			/* Choose p based on the CPU limit. */
//			maxrp = (opslimit / 4) / ((uint64_t)(1) << *logN);
//			if (maxrp > 0x3fffffff)
//				maxrp = 0x3fffffff;
//			*p = (uint32_t)(maxrp) / *r;
//		}
//
//	#ifdef DEBUG
//		fprintf(stderr, "N = %zu r = %d p = %d\n",
//		    (size_t)(1) << *logN, (int)(*r), (int)(*p));
//	#endif
//
//		/* Success! */
//		return (0);
//	}


	public static int pickparams(long maxmem, double maxmemfrac, double maxtime)
	{
		// results
		int logN;
		int r;
		int p;

		long memlimit;
		double opps;
		double opslimit;
		double maxN, maxrp;

		/* Figure out how much memory to use. */
		memlimit = 10000000;

		/* Figure out how fast the CPU is. */
		//		opps = scryptenc_cpuperf();
		//		
		//		opslimit = opps * maxtime;

		// let's use the minimum and see how it goes
		opslimit = 0;

		/* Allow a minimum of 2^15 salsa20/8 cores. */
		if(opslimit < 32768)
		{
			opslimit = 32768;
		}

		/* Fix r = 8 for now. */
		r = 8;

		/*
		 * The memory limit requires that 128Nr <= memlimit, while the CPU
		 * limit requires that 4Nrp <= opslimit.  If opslimit < memlimit/32,
		 * opslimit imposes the stronger limit on N.
		 */
		System.out.format("Requiring 128Nr <= %d, 4Nrp <= %f\n", memlimit, opslimit);
		
		if(opslimit < memlimit / 32)
		{
			/* Set p = 1 and choose N based on the CPU limit. */
			p = 1;
			maxN = opslimit / (r * 4);
			for(logN = 1; logN < 63; logN += 1)
			{
				if((1L << logN) > maxN / 2)
				{
					break;
				}
			}
		}
		else
		{
			/* Set N based on the memory limit. */
			maxN = memlimit / (r * 128);
			for(logN = 1; logN < 63; logN += 1)
			{
				if((1L << logN) > maxN / 2)
				{
					break;
				}
			}

			/* Choose p based on the CPU limit. */
			maxrp = (opslimit / 4) / (1L << logN);
			if(maxrp > 0x3fffffff)
			{
				maxrp = 0x3fffffff;
			}
			p = (int)maxrp / r;
		}

		System.out.format("N=%d r=%d p=%d\n", 1L << logN, r, p);
		
		System.out.println("128Nr=" + (128L * (1L << logN) * r));
		System.out.println("4Nrp=" + (4L * (1L << logN) * r * p));

		
		System.out.println("N=" + (1L << logN));
		System.out.println("logN=" + logN);
		System.out.println("r=" + r);
		System.out.println("p=" + p);

		return (0);
	}
	
	
	@Test
	public void test2() throws Exception
	{
		byte[] p = new byte[64];
		byte[] s = new byte[64];
		
		t(1, 1, 1, p, s);

		t(16384, 8, 32, p, s);       //  16M  3s
		
//		t(16384, 8, 64, p, s);       //  16M  6s
		
//		t(16384, 8, 128, p, s);      //  16M  12s
		
//		t(1,        1, 1, p, s);

//		t( 8196, 8,  32, p, s);      //   8M  1.6s
//		t( 8196, 8,  64, p, s);      //   8M  3.3s <-- good
//		t(16384, 8,  64, p, s);      //  16M  6s <-- better

//		t(16384, 8,   1, p, s);     //  16M  0.193s
//		t(16384, 8,  10, p, s);     //  16M  1.2s
//		t(16384, 8,  32, p, s);     //  16M  3.6s   <-- good
//		t(16384, 8, 100, p, s);     //  16M  10.9s
		
//		t(1,    10000, 1, p, s);    //  7.8M, 0.23s
//		t(1,    10000, 10, p, s);   //  7.8M, 1.17s
//		t(10000,    1, 1, p, s);    //    4M, 0.07s
//		t(10000,   10, 1, p, s);    //   16M, 0.10s
//		t(10000, 1000, 1, p, s);    // 1.2 G, 9.1s
//		t(1000000, 10, 1, p, s);    // 1.3 G? 8.2s
//		t(1<<20,    8, 1, p, s);    //    1G, 8.6s
	}
	
	
	private long used()
	{
		Runtime r = Runtime.getRuntime();
		return r.totalMemory() - r.freeMemory();
	}
	
	
	private void t(int n, int r, int p, byte[] pw, byte[] s)
	{
		SW sw = new SW();
		CKit.forceGC();
		
		long start = used();
		SCrypt.generate(pw, s, n, r, p, 32);
		long used = used() - start;
		
		// internet: Memory usage is approximately 128*r*N bytes
		
		D.print("N=", n, "r=", r, "p=", p, "used=", used / (1024f*1024f), "128*r*N=", 128L*r*n , "time=", sw);
	}
}
