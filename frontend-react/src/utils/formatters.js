export const formatDate = (dateArray) => {
  if (!dateArray) return '';
  const [year, month, day] = dateArray;
  return `${day}.${month}.${year}`;
};

export const formatTime = (timeArray) => {
  if (!timeArray) return '';
  const [hours, minutes] = timeArray;
  return `${hours}:${minutes.toString().padStart(2, '0')}`;
};

export const formatDateTime = (dateArray, timeArray) => {
  return `${formatDate(dateArray)} ${formatTime(timeArray)}`;
};

export const formatPrice = (price) => {
  return new Intl.NumberFormat('ru-RU', {
    style: 'currency',
    currency: 'RUB',
    minimumFractionDigits: 0
  }).format(price);
};