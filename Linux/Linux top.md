linux virt res shr����ֵ�ܴ����ʲô
VIRT��virtual memory usage �����ڴ�
1�����̡���Ҫ�ġ������ڴ��С����������ʹ�õĿ⡢���롢���ݵ�
2�������������100m���ڴ棬��ʵ��ֻʹ����10m����ô��������100m��������ʵ�ʵ�ʹ����
RES��resident memory usage ��פ�ڴ�
1�����̵�ǰʹ�õ��ڴ��С����������swap out
2�������������̵Ĺ���
3���������100m���ڴ棬ʵ��ʹ��10m����ֻ����10m����VIRT�෴
4�����ڿ�ռ���ڴ���������ֻͳ�Ƽ��صĿ��ļ���ռ�ڴ��С
SHR��shared memory �����ڴ�
1�������������̵Ĺ����ڴ棬Ҳ�����������̵Ĺ����ڴ�
2����Ȼ����ֻʹ���˼���������ĺ�������������������������Ĵ�С
3������ĳ��������ռ�������ڴ��С��ʽ��RES �C SHR
4��swap out�������ή����
DATA
1������ռ�õ��ڴ档���topû����ʾ����f��������ʾ������
2�������ĸó���Ҫ������ݿռ䣬��������������Ҫʹ�õġ�
top �����п���ͨ�� top ���ڲ�����Խ��̵���ʾ��ʽ���п��ơ��ڲ��������£�
s �C �ı仭�����Ƶ��
l �C �رջ�����һ���ֵ�һ�� top ��Ϣ�ı�ʾ
t �C �رջ�����һ���ֵڶ��� Tasks �͵����� Cpus ��Ϣ�ı�ʾ
m �C �رջ�����һ���ֵ����� Mem �� ������ Swap ��Ϣ�ı�ʾ
N �C �� PID �Ĵ�С��˳�����б�ʾ�����б�
P �C �� CPU ռ���ʴ�С��˳�����н����б�
M �C ���ڴ�ռ���ʴ�С��˳�����н����б�
h �C ��ʾ����
n �C �����ڽ����б�����ʾ���̵�����
q �C �˳� top
s �C �ı仭���������
��� ���� ����
a PID ����id
b PPID ������id
c RUSER Real user name
d UID ���������ߵ��û�id
e USER ���������ߵ��û���
f GROUP ���������ߵ�����
g TTY �������̵��ն��������Ǵ��ն������Ľ�������ʾΪ ?
h PR ���ȼ�
i NI niceֵ����ֵ��ʾ�����ȼ�����ֵ��ʾ�����ȼ�
j P ���ʹ�õ�CPU�����ڶ�CPU������������
k %CPU �ϴθ��µ����ڵ�CPUʱ��ռ�ðٷֱ�
l TIME ����ʹ�õ�CPUʱ���ܼƣ���λ��
m TIME+ ����ʹ�õ�CPUʱ���ܼƣ���λ1/100��
n %MEM ����ʹ�õ������ڴ�ٷֱ�
o VIRT ����ʹ�õ������ڴ���������λkb��VIRT=SWAP+RES
p SWAP ����ʹ�õ������ڴ��У��������Ĵ�С����λkb��
q RES ����ʹ�õġ�δ�������������ڴ��С����λkb��RES=CODE+DATA
r CODE ��ִ�д���ռ�õ������ڴ��С����λkb
s DATA ��ִ�д�������Ĳ���(���ݶ�+ջ)ռ�õ������ڴ��С����λkb
t SHR �����ڴ��С����λkb
u nFLT ҳ��������
v nDRT ���һ��д�뵽���ڣ����޸Ĺ���ҳ������
w S ����״̬����D=�����жϵ�˯��״̬��R=���У�S=˯�ߣ�T=����/ֹͣ��Z=��ʬ���̣�
x COMMAND ������/������
y WCHAN ���ý�����˯�ߣ�����ʾ˯���е�ϵͳ������
z Flags �����־���ο� sched.h
Ĭ������½���ʾ�Ƚ���Ҫ�� PID��USER��PR��NI��VIRT��RES��SHR��S��%CPU��%MEM��TIME+��COMMAND �С�����ͨ������Ŀ�ݼ���������ʾ���ݡ�
ͨ�� f ������ѡ����ʾ�����ݡ��� f ��֮�����ʾ�е��б����� a-z ������ʾ�����ض�Ӧ���У���󰴻س���ȷ����
�� o �����Ըı��е���ʾ˳�򡣰�Сд�� a-z ���Խ���Ӧ���������ƶ�������д�� A-Z ���Խ���Ӧ���������ƶ�����󰴻س���ȷ����
����д�� F �� O ����Ȼ�� a-z ���Խ����̰�����Ӧ���н������򡣶���д�� R �����Խ���ǰ������ת��