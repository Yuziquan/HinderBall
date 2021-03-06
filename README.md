## HinderBall
[![HinderBall](https://img.shields.io/badge/HinderBall-v1.0.0-brightgreen.svg)](https://github.com/Yuziquan/HinderBall)
[![license](https://img.shields.io/packagist/l/doctrine/orm.svg)](https://github.com/Yuziquan/HinderBall/blob/master/LICENSE)

> 详情移步：https://blog.csdn.net/wuchangi/article/details/81089342

<br/>

<img src="https://github.com/Yuziquan/HinderBall/blob/master/Screenshots/app_icon.png" width=150 height=150/>


### 一、项目功能

1. 实现一款人机对战模式的小球拦阻休闲游戏，使得多个不同颜色（颜色随机）的小球以一定速度按先后顺序由静止状态朝不同方向运动，玩家通过手指触摸屏幕以控制自身的滑块阻挡迎面而来的小球，小球会撞击滑块而发生反弹。同时，电脑所控制的滑块也会自动阻挡玩家反弹回去的小球。其中，若玩家没能成功阻挡一个小球，则电脑积分加一分；反之，玩家积分加一分。最终，在不同的赛制下，根据双方对小球的阻挡情况判定胜负。


<br/>

------
2. 为了增强玩家的游戏体验感，增加适当的游戏背景音乐、游戏中小球的撞击音效、小球没能被成功阻挡时的振动提醒、游戏音量的调节以及控制所有音乐\音效的开启\关闭的功能。此外，玩家还可以自行选择游戏赛制、游戏难度等。

<br/>

***

### 二、项目运行效果

*虽然已经用上了PS修图/抠图等技术，但自我感觉还是很丑，UI设计还是要锻炼......*

#### 1. 游戏主菜单（主界面）

<div align=center>
<img src="https://github.com/Yuziquan/HinderBall/blob/master/Screenshots/main_menu.jpg" width=250 height=440 />
</div>

<br/>

#### 2. 游戏设置界面

<div align=center>
<img src="https://github.com/Yuziquan/HinderBall/blob/master/Screenshots/game_settings.jpg" width=250 height=440 />
</div>

<br/>

#### 3. 游戏界面

<div align=center>
<img src="https://github.com/Yuziquan/HinderBall/blob/master/Screenshots/playing.jpg" width=250 height=440 />
</div>

<br/>

***

### 三、此项目的缺点

> * **UI设计**
>
>   此项上面也有提到，这里就不说了~~
>
>   <br/>
>
> * **电脑AI**
>
>   电脑所控制的滑块的移动轨迹是通过随机数确定的，使得只能通过提高电脑滑块的移动速度来提高对小球的成功截获率，进而提高游戏难度。玩家看到高速运动的电脑滑块会觉得有些不妥，使得游戏体验感不佳。
>
>   <br/>
>
>   （简单改进：可用实现一个对电脑滑块周围的小球的所在位置的探测机制，借助该机制，结合上随机算法的使用，使得电脑滑块可以根据迎面而来的小球的位置提前做好阻挡的前期准备，也就是停在预测小球会到达的位置。同时通过随机算法对滑块停放位置进行影响，使之在原来的预测位置的基础上产生额外的位移，使得滑块对小球的截获率降低。而且，随机算法影响越大，则电脑滑块对小球的截获率越低，游戏难度级别越低。此时滑块的速度就显得合情合理，玩家体验感便上升了。）
>
>   <br/>
>
>   (进一步改进：可以借助机器学习的相关技术，来实现电脑滑块对小球的拦阻技术。)
>
>   ​
