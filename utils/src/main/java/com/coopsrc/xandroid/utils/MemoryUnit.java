package com.coopsrc.xandroid.utils;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-29 16:03
 */
public enum MemoryUnit {

    BYTE {
        @Override
        public long toBytes(long value) {
            return value;
        }

        @Override
        public long toKiloByte(long value) {
            return value / (KB / B);
        }

        @Override
        public long toMegaByte(long value) {
            return value / (MB / B);
        }

        @Override
        public long toGigaByte(long value) {
            return value / (GB / B);
        }

        @Override
        public long toTeraByte(long value) {
            return value / (TB / B);
        }

        @Override
        public long toPetaByte(long value) {
            return value / (PB / B);
        }

        @Override
        public long toExaByte(long value) {
            return value / (EB / B);
        }

        @Override
        public long convert(long value, MemoryUnit unit) {
            return unit.toBytes(value);
        }
    },
    KILO_BYTE {
        @Override
        public long toBytes(long value) {
            return x(value, KB / B, MAX / (KB / B));
        }

        @Override
        public long toKiloByte(long value) {
            return value;
        }

        @Override
        public long toMegaByte(long value) {
            return value / (MB / KB);
        }

        @Override
        public long toGigaByte(long value) {
            return value / (GB / KB);
        }

        @Override
        public long toTeraByte(long value) {
            return value / (TB / KB);
        }

        @Override
        public long toPetaByte(long value) {
            return value / (PB / KB);
        }

        @Override
        public long toExaByte(long value) {
            return value / (EB / KB);
        }

        @Override
        public long convert(long value, MemoryUnit unit) {
            return unit.toKiloByte(value);
        }
    },
    MEGA_BYTE {
        @Override
        public long toBytes(long value) {
            return x(value, MB / B, MAX / (MB / B));
        }

        @Override
        public long toKiloByte(long value) {
            return x(value, MB / KB, MAX / (MB / KB));
        }

        @Override
        public long toMegaByte(long value) {
            return value;
        }

        @Override
        public long toGigaByte(long value) {
            return value / (GB / MB);
        }

        @Override
        public long toTeraByte(long value) {
            return value / (TB / MB);
        }

        @Override
        public long toPetaByte(long value) {
            return value / (PB / MB);
        }

        @Override
        public long toExaByte(long value) {
            return value / (EB / MB);
        }

        @Override
        public long convert(long value, MemoryUnit unit) {
            return unit.toMegaByte(value);
        }
    },
    GIGA_BYTE {
        @Override
        public long toBytes(long value) {
            return x(value, GB / B, MAX / (GB / B));
        }

        @Override
        public long toKiloByte(long value) {
            return x(value, GB / KB, MAX / (GB / KB));
        }

        @Override
        public long toMegaByte(long value) {
            return x(value, GB / MB, MAX / (GB / MB));
        }

        @Override
        public long toGigaByte(long value) {
            return value;
        }

        @Override
        public long toTeraByte(long value) {
            return value / (TB / GB);
        }

        @Override
        public long toPetaByte(long value) {
            return value / (PB / GB);
        }

        @Override
        public long toExaByte(long value) {
            return value / (EB / GB);
        }

        @Override
        public long convert(long value, MemoryUnit unit) {
            return unit.toGigaByte(value);
        }
    },
    TERA_BYTE {
        @Override
        public long toBytes(long value) {
            return x(value, TB / B, MAX / (TB / B));
        }

        @Override
        public long toKiloByte(long value) {
            return x(value, TB / KB, MAX / (TB / KB));
        }

        @Override
        public long toMegaByte(long value) {
            return x(value, TB / MB, MAX / (TB / MB));
        }

        @Override
        public long toGigaByte(long value) {
            return x(value, TB / GB, MAX / (TB / GB));
        }

        @Override
        public long toTeraByte(long value) {
            return value;
        }

        @Override
        public long toPetaByte(long value) {
            return value / (PB / TB);
        }

        @Override
        public long toExaByte(long value) {
            return value / (EB / TB);
        }

        @Override
        public long convert(long value, MemoryUnit unit) {
            return unit.toTeraByte(value);
        }
    },
    PETA_BYTE {
        @Override
        public long toBytes(long value) {
            return x(value, PB / B, MAX / (PB / B));
        }

        @Override
        public long toKiloByte(long value) {
            return x(value, PB / KB, MAX / (PB / KB));
        }

        @Override
        public long toMegaByte(long value) {
            return x(value, PB / MB, MAX / (PB / MB));
        }

        @Override
        public long toGigaByte(long value) {
            return x(value, PB / GB, MAX / (PB / GB));
        }

        @Override
        public long toTeraByte(long value) {
            return x(value, PB / TB, MAX / (PB / TB));
        }

        @Override
        public long toPetaByte(long value) {
            return value;
        }

        @Override
        public long toExaByte(long value) {
            return value / (EB / PB);
        }

        @Override
        public long convert(long value, MemoryUnit unit) {
            return unit.toPetaByte(value);
        }
    },
    EXA_BYTE {
        @Override
        public long toBytes(long value) {
            return x(value, EB / B, MAX / (EB / B));
        }

        @Override
        public long toKiloByte(long value) {
            return x(value, EB / KB, MAX / (EB / KB));
        }

        @Override
        public long toMegaByte(long value) {
            return x(value, EB / MB, MAX / (EB / MB));
        }

        @Override
        public long toGigaByte(long value) {
            return x(value, EB / GB, MAX / (EB / GB));
        }

        @Override
        public long toTeraByte(long value) {
            return x(value, EB / TB, MAX / (EB / TB));
        }

        @Override
        public long toPetaByte(long value) {
            return x(value, EB / PB, MAX / (EB / PB));
        }

        @Override
        public long toExaByte(long value) {
            return value;
        }

        @Override
        public long convert(long value, MemoryUnit unit) {
            return unit.toExaByte(value);
        }
    },
    ;

    private static final long UNIT = 10L;

    private static final long B = 1L;
    private static final long KB = B << UNIT;
    private static final long MB = KB << UNIT;
    private static final long GB = MB << UNIT;
    private static final long TB = GB << UNIT;
    private static final long PB = TB << UNIT;
    private static final long EB = PB << UNIT;
    private static final long MAX = Long.MAX_VALUE;


    private static long x(long value, long unit, long limit) {
        if (value > +limit) {
            return Long.MAX_VALUE;
        }

        if (value < -limit) {
            return Long.MIN_VALUE;
        }

        return value * unit;
    }

    public long toBytes(long value) {
        throw new AbstractMethodError();
    }

    public long toKiloByte(long value) {
        throw new AbstractMethodError();
    }

    public long toMegaByte(long value) {
        throw new AbstractMethodError();
    }

    public long toGigaByte(long value) {
        throw new AbstractMethodError();
    }

    public long toTeraByte(long value) {
        throw new AbstractMethodError();
    }

    public long toPetaByte(long value) {
        throw new AbstractMethodError();
    }

    public long toExaByte(long value) {
        throw new AbstractMethodError();
    }

    public long convert(long value, MemoryUnit unit) {
        throw new AbstractMethodError();
    }

    public static String format(long value) {
        double bytes = value * 1.0d;
        double kiloByte = bytes / 1024.0d;
        double megaByte = kiloByte / 1024.0d;
        double gigaByte = megaByte / 1024.0d;
        double teraByte = gigaByte / 1024.0d;
        double petaByte = teraByte / 1024.0d;
        double exaByte = petaByte / 1024.0d;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if (exaByte > 1) {
            return String.format(Locale.getDefault(), "%s EB", decimalFormat.format(exaByte));
        } else if (petaByte > 1) {
            return String.format(Locale.getDefault(), "%s PB", decimalFormat.format(petaByte));
        } else if (teraByte > 1) {
            return String.format(Locale.getDefault(), "%s TB", decimalFormat.format(teraByte));
        } else if (gigaByte > 1) {
            return String.format(Locale.getDefault(), "%s GB", decimalFormat.format(gigaByte));
        } else if (megaByte > 1) {
            return String.format(Locale.getDefault(), "%s MB", decimalFormat.format(megaByte));
        } else {
            return String.format(Locale.getDefault(), "%s KB", decimalFormat.format(kiloByte));
        }
    }

    public static String formatSpeed(long value) {
        double bytes = value * 1.0d;
        double kiloByte = bytes / 1024.0d;
        double megaByte = kiloByte / 1024.0d;
        double gigaByte = megaByte / 1024.0d;
        double teraByte = gigaByte / 1024.0d;
        double petaByte = teraByte / 1024.0d;
        double exaByte = petaByte / 1024.0d;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if (exaByte > 1) {
            return String.format(Locale.getDefault(), "%s EB/s", decimalFormat.format(exaByte));
        } else if (petaByte > 1) {
            return String.format(Locale.getDefault(), "%s PB/s", decimalFormat.format(petaByte));
        } else if (teraByte > 1) {
            return String.format(Locale.getDefault(), "%s TB/s", decimalFormat.format(teraByte));
        } else if (gigaByte > 1) {
            return String.format(Locale.getDefault(), "%s GB/s", decimalFormat.format(gigaByte));
        } else if (megaByte > 1) {
            return String.format(Locale.getDefault(), "%s MB/s", decimalFormat.format(megaByte));
        } else {
            return String.format(Locale.getDefault(), "%s KB/s", decimalFormat.format(kiloByte));
        }
    }
}
