package com.example.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen()
                }
            }
        }
    }
}

// بيانات الحالات ماشية زي ما هي
val categories: Map<String, List<String>> = mapOf(
    "عامة" to listOf(
        "الدنيا علمتني إني ماشتكيش، وإن شكيت أشكي لربنا بس.",
        "ماتقولش ظروفي صعبة، قول الحمد لله على كل حال.",
        "اللي بيروح لحد ما يجيش، مش خسارة إنك تسيبه.",
        "مش كل اللي بيضحك مبسوط، ومش كل اللي ساكت زعلان.",
        "كون جدع، حتى لو الدنيا مش جدعة معاك.",
        "الصبر مفتاح الفرج، والله ما بينسى حد.",
        "اللي فات مات، واللي جاي في إيد ربنا.",
        "القناعة كنز ما يفنى.",
        "ماتخليش حد يرسملك مستقبلك، إنت اللي ماسك القلم.",
        "خلي عندك مبدأ، وألف صاحب مش يستاهلوك."
    ),
    "دينية" to listOf(
        "سبحان الله وبحمده، سبحان الله العظيم.",
        "الحمد لله على كل حال، وفي كل حال.",
        "لا حول ولا قوة إلا بالله.",
        "اللهم إني أسألك العفو والعافية في الدنيا والآخرة.",
        "توكلت على الله، وهو حسبي ونعم الوكيل.",
        "اللهم اجعلنا من الذاكرين الشاكرين.",
        "الدنيا ساعة، اجعلها طاعة.",
        "من لزم الاستغفار، جعل الله له من كل هم فرجاً.",
        "أستغفر الله العظيم وأتوب إليه.",
        "اللهم إنك عفو تحب العفو فاعف عنا."
    ),
    "رومانسية" to listOf(
        "لو الحب اختيار، كنت اخترتك كل مرة.",
        "إنتي مش تفاصيل يومي، إنتي اليوم بحاله.",
        "قلبي مش محطة، بس إنت الوحيد اللي مر من هنا وعجبه المكان.",
        "كل ما أفكر فيك، بحس إن الدنيا حلوة.",
        "لو الدنيا وردة، إنتي ريحتها.",
        "مش بحب الكلام الكتير، بحب اللي يفهم من نظرة.",
        "إنتي السبب إن ابتسامتي بقت صاحية بدري.",
        "بحبك بطريقة مش محتاجة شرح، محتاجة إحساس بس.",
        "كتير ناس قابلوني، بس إنت الوحيد اللي فضلت.",
        "القلب اللي بيحبش، مش بيبقى قلب."
    ),
    "ضحك" to listOf(
        "أنا مش كسول، أنا في وضع توفير الطاقة.",
        "الفلوس مش بتشتري السعادة، بس بتشتري حاجات أحلى من السعادة.",
        "النوم مش عيب، العيب إنك تصحى بدري.",
        "أنا رياضيّ بطبعه، بريّح على طول.",
        "لو الدنيا ورق، أنا ضهر الصفحة.",
        "بفكر أعمل رجيم، بس التفكير بيجيبلي جوع.",
        "أنا مش عصبي، أنا بس في بيئة غير مناسبة.",
        "المثل بيقول: اللي يستنى حاجة، بتجيله حاجة تانية.",
        "مش فاضي للمشاكل، عندي مصاريف أدور عليها.",
        "ولادي سابوا مية الزمزم، شربوا البيبسي."
    ),
    "شغل" to listOf(
        "اللي بيتعب صح، بيرتاح صح.",
        "النجاح مش بالحظ، بالصبر والاجتهاد.",
        "ماتقولش مش هقدر، جرب الأول.",
        "الشغل عبادة لما تكون نيتك حلال.",
        "التعب النهاردة، راحة بكرة.",
        "الإتقان مش رفاهية، هو الأساس.",
        "اللي بيجري برجليه، بيوصل أسرع من اللي بيسأل الطريق.",
        "الطموح مش عيب، العيب الرضا بالقليل وأنت تقدر على أكتر.",
        "كل بداية صعبة، وكل صعب يهون.",
        "خطط، اشتغل، استمر، وربنا يكرم."
    )
)

@Composable
fun HomeScreen() {
    val context = LocalContext.current

    val categoryNames = categories.keys.toList()
    var selectedCategory by remember { mutableIntStateOf(0) }
    var statusIndex by remember { mutableIntStateOf(0) }

    val currentList = categories[categoryNames[selectedCategory]] ?: emptyList()
    val currentStatus = currentList[statusIndex.coerceIn(0, currentList.lastIndex)]

    // خلفية متدرجة
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF0F2027),
                        Color(0xFF203A43),
                        Color(0xFF2C5364),
                        Color(0xFF6A3093),
                        Color(0xFFA044FF)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // العنوان
            Text(
                text = "✨ حالات وكلام",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            Text(
                text = "اختار فئتك وشاركها مع أصحابك",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // تابات بشكل حبوب
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categoryNames.indices.toList()) { i ->
                    val selected = i == selectedCategory
                    Surface(
                        shape = CircleShape,
                        color = if (selected) Color.White
                                else Color.White.copy(alpha = 0.15f),
                        border = BorderStroke(
                            1.dp,
                            if (selected) Color.White.copy(alpha = 0.5f)
                            else Color.White.copy(alpha = 0.2f)
                        )
                    ) {
                        Text(
                            text = categoryNames[i],
                            color = if (selected) Color(0xFF203A43)
                                    else Color.White,
                            fontWeight = if (selected) FontWeight.Bold
                                         else FontWeight.Normal,
                            modifier = Modifier.padding(
                                horizontal = 18.dp,
                                vertical = 8.dp
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // كارت الحالة الرئيسي
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                border = BorderStroke(
                    1.dp,
                    Color.White.copy(alpha = 0.25f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // علامة اقتباس
                        Text(
                            text = "❝",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = currentStatus,
                            color = Color.White,
                            fontSize = 24.sp,
                            lineHeight = 38.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "· ${categoryNames[selectedCategory]} ·",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // أزرار أسفل
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // زر حالة جديدة
                Button(
                    onClick = { statusIndex = (statusIndex + 1) % currentList.size },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = "🔄 حالة جديدة",
                        color = Color(0xFF203A43),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // نسخ
                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("status", currentStatus))
                        Toast.makeText(context, "اتنسخت ✅", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.6f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text("📋 نسخ", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                // مشاركة
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, currentStatus)
                        }
                        context.startActivity(Intent.createChooser(intent, "شارك الحالة"))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA044FF)
                    )
                ) {
                    Text("📤 مشاركة", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
