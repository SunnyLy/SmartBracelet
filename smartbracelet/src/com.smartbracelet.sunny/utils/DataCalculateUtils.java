package com.smartbracelet.sunny.utils;

import android.text.TextUtils;

import com.smartbracelet.sunny.model.BloodPressure;
import com.smartbracelet.sunny.model.UserModel;

import java.util.Date;

/**
 * Created by sunny on 2015/12/15.
 * 数据计算工具类
 * 1），计算能耗
 * 2），计算呼吸频率
 * 3），计算疲劳度
 * 4），计算情绪
 * 5），计算血压
 * 6），计算心率
 * 7），计算计步中活动量得分
 */
public class DataCalculateUtils {

    private static final int HEART_RATE_NORMAL = 1;
    private static final int HEART_RATE_QUICK = 2;
    private static double breathRate = 0;

    /**
     * 1.计算能量消耗
     * 公式：
     * 男性 ：  步能量消耗（千卡）=0.53*身高（厘米） + 0.58*体重（公斤）+0.37*步频（步/分钟）+1.51*时间(分钟)-145.03
     * 女性 :     步行能力消耗(千卡)=0.003*身高(厘米)+0.45*体重(公斤)+0.16/步频(步/分钟)+0.39*时间(分钟)-12.93
     * 我们现在显示的卡路里.
     * 在运动和饮食中，常常遇到几个热量的词：卡路里、大卡、卡、千焦、焦耳热量单位换算；
     * 千卡=卡路里
     * 千卡=大卡
     * 1千卡=1000卡
     * 1千焦=1000焦耳
     * 1千卡1大卡1卡路里＝1000卡
     * 1千卡/1大卡/1卡路里（kcal）=4.184千焦（kJ）
     * 1卡＝4.182焦耳
     * 如: 汉堡包/个248千卡
     * 　　比萨/小份302千卡
     * 　　意大利通心粉/份500千卡
     */
    public static String calculateEnergy(UserModel userModel, int totalStep, long totalTime) {
        String energy = "";
        String sex = userModel.getSex();
        String height = userModel.getHeight();
        String weight = userModel.getWeight();
        if ("1".equals(sex)) {
            //男
            energy = (0.53 * Integer.valueOf(height) +
                    0.58 * Integer.valueOf(weight) +
                    0.37 * (totalStep / ((totalTime / 1000) / 60)) +
                    1.51 * ((totalTime / 1000) / 60) - 145.03) + "";
        } else if ("0".equals(sex)) {
            //女
            energy = (0.003 * Integer.valueOf(height) +
                    0.45 * Integer.valueOf(weight) +
                    0.16 * (totalStep / ((totalTime / 1000) / 60)) +
                    0.39 * ((totalTime / 1000) / 60) - 12.93) + "";
        }

        return energy;
    }

    /**
     * 2.计算呼吸频率
     * 参考公式：
     * 呼吸频率：
     * 儿童呼吸频率： ２０次
     * 成年人呼吸频率： 1２-20 次/秒
     * 女人比男人多１－２次／分钟
     * 呼吸频率与步数之间关系：　３－４步为１次呼吸频率，　如果没分钟跑６０步左右，　则呼吸频率在６０／３－４　＝　１５－２０次．所以我们暂时３．５
     * 年龄段
     * <p>儿童阶段:</p>
     * 1—6 岁 幼儿    步数与呼吸频率3, 即: 30步/3
     * 6岁—14岁 儿童    步数与呼吸频率3.15, 即: 30步/3.15
     * 成人:
     * 14.15岁—25岁 青年期  步数与呼吸频率3.15, 即: 30步/3.3
     * 25—65岁 成年期  步数与呼吸频率3.15, 即: 30步/3.6
     * 老年人:
     * 65岁以后 老年期   步数与呼吸频率3.15, 即: 30步/4
     */
    public static double calculateBreathRate(UserModel userModel, int totalStep) {

        double rate = getBreathRate(userModel.getBirthday());
        return rate * totalStep / 30;
    }

    /**
     * 获取呼吸频率
     *
     * @param birthday
     * @return
     */
    private static double getBreathRate(String birthday) {
        int age = 1;
        if (TextUtils.isEmpty(birthday)) {
            age = 1;
        } else {
            Date date = DateTime.getDateByFormat(birthday, DateTime.DEFYMD);
            age = DateTime.getAge(date);
        }

        if (age >= 1 && age <= 6) {
            breathRate = 3;
        } else if (age > 6 && age <= 14) {
            breathRate = 3.15;
        } else if (age > 14 && age <= 25) {
            breathRate = 3.3;
        } else if (age > 25 && age <= 65) {
            breathRate = 3.6;
        } else if (age > 65) {
            breathRate = 4;
        }

        return breathRate;
    }

    /**
     * 3.计算疲劳度
     * 公式：
     * 疲劳度的计算算法
     * <p/>
     * <p/>
     * 疲劳程度                        收宿压                                             舒张压
     * <p/>
     * 正常                         120.05±1.11                                        75.32±0.92
     * 提示： 您的身心正常，状态很好！
     * <p/>
     * 轻度疲劳                （ 120.05±1.11） * （1+0.035）          （75.32±0.92）* （1+0.035）
     * 提示： 您的身心已经开始有疲惫的倾向了！ 精神比较饱满、有点疲惫感、注意力开始不集中啦。
     * <p/>
     * 中度疲劳                 （ 120.05±1.11）  * （1+0.037）         （75.32±0.92） * （1+0.037）
     * 提示： 您的身心有些疲劳，如果能停下来，就休息一下吧。精神不饱满、或有有疲乏、腿疼的感觉，注意力和效率低下。
     * <p/>
     * 重度疲劳                 （ 120.05±1.11） * （1+0.04）           （75.32±0.92）* （1+0.04）
     * 提示： 您的身心已感觉到很疲劳，请停下来休息，否则会影响您的身心健康。
     * <p/>
     * 极重度疲劳              （ 120.05±1.11） * （1+0.042）         （ 75.32±0.92）* （1+0.042）
     * 提示： 您感觉眼睛酸痛、发胀、干涩、视力模糊。 需要马上停下来休息！
     */
    public static String calculateTired(String sbp, String dbp) {
        String tiredInfo = "您的身心正常，状态很好！";
        //正常的收缩压与舒张压
        double normalSMax = 120.05 + 1.11;
        double normalSMin = 120.05 - 1.11;
        double normalDMax = 75.32 + 0.92;
        double normalDMin = 75.32 - 0.92;
        //轻度疲劳
        double littleTiredSMax = normalSMax * (1 + 0.035);
        double littleTiredSMin = normalSMin * (1 + 0.035);
        double littleTiredDMax = normalDMax * (1 + 0.035);
        double littleTiredDMin = normalDMin * (1 + 0.035);
        //中度疲劳
        double middleTiredSMax = normalSMax * (1 + 0.037);
        double middleTiredSMin = normalSMin * (1 + 0.037);
        double middleTiredDMax = normalDMax * (1 + 0.037);
        double middleTiredDMin = normalDMin * (1 + 0.037);
        //重度疲劳
        double highTiredSMax = normalSMax * (1 + 0.04);
        double highTiredSMin = normalSMin * (1 + 0.04);
        double highTiredDMax = normalDMax * (1 + 0.04);
        double highTiredDMin = normalDMin * (1 + 0.04);
        //极度疲劳
        double xHighTiredSMax = normalSMax * (1 + 0.042);
        double xHighTiredSMin = normalSMin * (1 + 0.042);
        double xHighTiredDMax = normalDMax * (1 + 0.042);
        double xHighTiredDMin = normalDMin * (1 + 0.042);

        double sdbDouble = Double.parseDouble(sbp);
        double ddbDouble = Double.parseDouble(dbp);

        //正常
        if ((sdbDouble <= normalSMax) && (ddbDouble <= normalDMax)) {
            return tiredInfo;
        }

        //轻度疲劳
        if (((sdbDouble > normalSMax) && (sdbDouble <= littleTiredSMax)) &&
                (((ddbDouble > normalDMax) && (ddbDouble <= littleTiredDMax)))) {
            tiredInfo = "您的身心已经开始有疲惫的倾向了！ 精神比较饱满、有点疲惫感、注意力开始不集中啦。";
            return tiredInfo;
        }

        //中度疲劳
        if (((sdbDouble > littleTiredSMax) && (sdbDouble <= middleTiredSMax)) &&
                (((ddbDouble > littleTiredDMax) && (ddbDouble <= middleTiredDMax)))) {
            tiredInfo = "您的身心有些疲劳，如果能停下来，就休息一下吧。精神不饱满、或有有疲乏、腿疼的感觉，注意力和效率低下。";
            return tiredInfo;
        }

        //重度疲劳
        if (((sdbDouble > middleTiredSMax) && (sdbDouble <= highTiredSMax)) &&
                (((ddbDouble > middleTiredDMax) && (ddbDouble <= highTiredDMax)))) {
            tiredInfo = "您的身心已感觉到很疲劳，请停下来休息，否则会影响您的身心健康。";
            return tiredInfo;
        }

        //极度疲劳
        if (((sdbDouble > highTiredSMax) && (sdbDouble <= xHighTiredSMax)) &&
                (((ddbDouble > highTiredDMax) && (ddbDouble <= xHighTiredDMax)))) {
            tiredInfo = "您感觉眼睛酸痛、发胀、干涩、视力模糊。 需要马上停下来休息！";
            return tiredInfo;
        }

        return tiredInfo;

    }

    /**
     * 4.计算情绪
     * 公式：
     * 情绪主要根据心率和呼吸频率计算 ：
     * 平和:   心率处于正常值范围, 且呼吸频率处于正常值;
     * 激动:   心率急速或快速, 且呼吸频率超过正常范围;
     * 消沉:    心率成人低于60, 呼吸频率低于15次/分;  老人低于50,呼吸12次/分.
     * <p/>
     * <p/>
     * 。新生儿每分钟40～50次;1岁以内，30～40次/分;2～3岁，25～30次/分;4 ～7，20岁～25次/分;7岁以上同成年人。
     * 儿童呼吸频率： ２０次
     * 成年人呼吸频率： 1２-20 次/分钟
     * 女人比男人多１－２次／分钟
     * <p/>
     * <p/>
     * 心率：
     * 健康成人的心率为60～100次/分，
     * 大多数为60～80次/分，女性稍快；
     * 3岁以下的小儿常在100次/分以上;
     * 成人每分钟心率超过100次（一般不超过 160次/分）或婴幼儿超过 150次/分者，称为窦性心动过速。
     * 心率在 160～220次/分，常称为阵发性心动过速
     * 心率低于60次/分者（一般在40次/分以上），称为窦性心动过缓。
     */
    public static String calculateMood(UserModel userModel, String heartRate) {

        String moodInfo = "";
        String birthday = userModel.getBirthday();
        double breathRate = getBreathRate(birthday);
        int heart = Integer.valueOf(heartRate);
        Date date = DateTime.getDateByFormat(birthday, DateTime.DEFYMD);
        int age = DateTime.getAge(date);
        if (age <= 1) {
            //1岁内新生儿(30~40次/分)
            if (breathRate >= 30 && breathRate <= 40 && heart >= 100) {
                moodInfo = "平和";
            } else if (breathRate > 40) {
                moodInfo = "激动";
            }


        } else if (age >= 2 && age <= 3) {
            //2~3岁内（25~30次/分)
            if (breathRate >= 25 && breathRate <= 30 && heart >= 100) {
                moodInfo = "平和";
            } else if (breathRate > 30 && heart > 160) {
                moodInfo = "激动";
            }
        } else if (age >= 4 && age <= 7) {
            //4~7岁内(20~25次/分）
        } else if (age > 8) {
            //7岁以上与成人一样(12 ~20次/分）
            if (age >= 60) {
                //成年人
                if (heart < 50 && breathRate < 15) {
                    moodInfo = "消沉";
                } else if (heart > 100 && breathRate > 20) {
                    moodInfo = "激动";
                } else if ((heart >= 60 && heart <= 80) && (breathRate >= 12 && breathRate <= 20)) {
                    moodInfo = "平和";
                }
            }

        }

        return moodInfo;
    }

    /**
     * 5.计算血压
     * 公式：
     * 正常成年人的收缩压为90—140毫米汞柱，舒张压为60—90毫米汞柱.
     * <p/>
     * 成年人:   收缩压低于90毫米汞柱、舒张压低于60毫米汞柱，就叫“低血压”。
     * 提示信息如下： 病情轻微症状可有：头晕、头痛、食欲不振、疲劳、脸色苍白、消化不良、晕车船等；
     * 严重症状包括：直立性眩晕、四肢冷、心悸、呼吸困难、共济失调、发音含糊、甚至昏厥、需长期卧床。
     * 主要原因：血压下降，导致血液循环缓慢，远端毛细血管缺血，以致影响组织细胞氧气和营养的供应，二氧化碳及代谢废物的排泄。
     * 主要危害：视力、听力下降，诱发或加重老年性痴呆，头晕、昏厥、跌倒、骨折发生率大大增加。
     * 成年人：收缩压>140mmHg（18.7Kpa)或舒张压90mmHg（12.0Kpa)即可认为是高血压
     * 表现为头晕、头痛、心悸等，随着受损器官出现的并发症而有明显的症状。
     * 注意: 饮食 低盐、低脂、低胆固醇、低热量的饮食，以植物油为主，减少含饱和脂肪酸的肥肉或肉类制品，
     * 动物内脏含胆固醇高，应少吃，多进食高维生素的食物，如蔬菜、水果、新鲜乳类。忌饮咖啡、浓茶、酒等刺激性食物，酒已被认为是高血压的发病因素应戒酒。
     */
    public static String calculateBloothPressure(UserModel userModel, String sdp, String ddp) {

        String bloothPressureInfo = "";
        Date date = DateTime.getDateByFormat(userModel.getBirthday(), DateTime.DEFYMD);
        int age = DateTime.getAge(date);
        int sbdp = Integer.valueOf(sdp);
        int dbdp = Integer.valueOf(ddp);
        if ((sbdp >= 90 && sbdp <= 140) && (dbdp >= 60 && dbdp <= 90)) {
            bloothPressureInfo = "血压正常";
        } else if (sbdp < 90 && dbdp < 60) {
            bloothPressureInfo = "病情轻微症状可有：头晕、头痛、食欲不振、疲劳、脸色苍白、消化不良、晕车船等；\n" +
                    "严重症状包括：直立性眩晕、四肢冷、心悸、呼吸困难、共济失调、发音含糊、甚至昏厥、需长期卧床。\n" +
                    "主要原因：血压下降，导致血液循环缓慢，远端毛细血管缺血，以致影响组织细胞氧气和营养的供应，二氧化碳及代谢废物的排泄。\n" +
                    "主要危害：视力、听力下降，诱发或加重老年性痴呆，头晕、昏厥、跌倒、骨折发生率大大增加。";
        } else if (sbdp > 140 && dbdp > 90) {
            bloothPressureInfo = "表现为头晕、头痛、心悸等，随着受损器官出现的并发症而有明显的症状。\n" +
                    "注意: 饮食 低盐、低脂、低胆固醇、低热量的饮食，以植物油为主，减少含饱和脂肪酸的肥肉或肉类制品，\n" +
                    "动物内脏含胆固醇高，应少吃，多进食高维生素的食物，如蔬菜、水果、新鲜乳类。忌饮咖啡、浓茶、酒等刺激性食物，酒已被认为是高血压的发病因素应戒酒。";
        }

        return bloothPressureInfo;
    }

    /**
     * 6.计算心率
     * 公式：
     * 心率正常:
     * 1、正常成年人安静时的心率平均在75次/分左右(60—100次/分之间)。
     * 2、初生儿的心率很快，可达130次/分以上。
     * 3、3岁以下的小儿常在100次/分以上；
     * 4、老年人心率40-80次/分之间;
     * 在成年人中，女性的心率一般比男性稍快。同一个人，在安静或睡眠时心率减慢，运动时或情绪激动时心率加快，
     * 在某些药物或神经体液因素的影响下，会使心率发生加快或减慢。经常进行体力劳动和体育锻炼的人，平时心率较慢。
     * <p/>
     * 心率快速:  成人每分钟心率超过100次（一般不超过 160次/分）或婴幼儿超过 150次/分者;
     * 常见于正常人运动、兴奋、激动、吸烟、饮酒和喝浓茶后。也可见于发热、休克、贫血、甲亢、
     * 心力衰竭及应用阿托品、肾上腺素、麻黄素等。
     * 心率急速:心率在160-220次/分,  常见于正常人运动、兴奋、激动、吸烟、饮酒和喝浓茶后。
     * 也可见于发热、休克、贫血、甲亢、心力衰竭及应用阿托品、肾上腺素、麻黄素等。
     * 心率过缓: 心率低于60次/分者（一般在40次/分以上）,可见于长期从事重体力劳动和运动员；
     * 病理性的见于甲状腺机能低下、颅内压增高、阻塞性黄疸、以及洋地黄、奎尼丁或心得安类药物过量或中毒。
     */
    public static String calculateHeartRate(UserModel userModel, String heart) {
        if (TextUtils.isEmpty(heart)) {
            throw new NullPointerException("the heart may not be null");
        }
        String heartRateInfo = "";
        int heartRate = Integer.valueOf(heart);
        Date date = DateTime.getDateByFormat(userModel.getBirthday(), DateTime.DEFYMD);
        int age = DateTime.getAge(date);
        //先算正常的
        if (age <= 1) {
            if (heartRate >= 130 && heartRate < 150) {
                heartRateInfo = "心率正常";
            } else if (heartRate > 150) {
                heartRateInfo = "心率快速";
            }
        } else if (age > 1 && age <= 3) {
            if (heartRate > 100) {
                heartRateInfo = "心率正常";
            }
        } else if (age >= 4 && age < 60) {
            if (heartRate >= 60 && heartRate <= 100) {
                heartRateInfo = "心率正常";
            } else if (heartRate > 100) {
                heartRateInfo = "心率快速";
            } else if (heartRate < 60) {
                heartRateInfo = "心率过缓";
            }
        } else if (age >= 60) {
            if (heartRate >= 40 && heartRate <= 60) {
                heartRateInfo = "心率正常";
            } else if (heartRate > 100) {
                heartRateInfo = "心率快速";
            }
        }


        return heartRateInfo;
    }

    /**
     * 7.计算计步得分
     * 公式：
     * 计步中活动量计算方法:
     * 步行：如果是悠闲缓慢的散步，每小时记3分；(每小时6公里/小时以下速度为快步)
     * 快步走：每小时记5分。(每小时6-7公里/小时的速度为快步)
     * 慢跑：每小时,得分 6分; (以7-9公里/小时的速度为快为慢跑)
     * 快跑：每小时, 得分7分; (以10公里/小时以上的速度为快为慢跑)
     */
    public static int calculateStepGrade(int totalStep, long totalTimes) {

        int grade = 0;

        int hour = (int) ((totalTimes / 1000) / 60 / 60);
        int volley = totalStep / hour;
        if (volley < 6) {
            grade = 3;
        } else if (volley >= 6 && volley < 7) {
            grade = 5;
        } else if (volley >= 7 && volley < 9) {
            grade = 6;
        } else if (volley >= 10) {
            grade = 7;
        }

        return grade;
    }
}
